# LOG ANALYSIS

----

## 입력 포맷

- I / O 처리
- 입력은 samplelog.log를 사용
- 입력 구문 형식 : [상태코드] [URL] [웹브라우저] [호출시간]

---
## 출력 포맷
- 파일 I / O 처리
- 호출되는 상태코드가 200일 경우에만 정상으로 Count
- 최다호출 APIKEY : 호출횟수가 가장 많은 APIKEY
- 상위 3개의 API ServiceID와 각각의 요청 수 : 요청 수가 상위 3위 이내인 API ServiceID와 요청 횟수 출력. 
- 요청 횟수가 동일한 경우가 발생하면 어떤 것을 출력해도 상관없음
- 웹브라우저별 사용 비율 : 정상요청인 경우, 요청 브라우저의 사용 비율

---

### 1. 로그 읽기
samplelog.log 파일을 resources 폴더에 저장

**InputApiLog**
```java
    @Getter
    @Builder
    public class InputApiLog {
        private String code;
        private URL url;
        private String WebBrowser;
        private LocalDateTime accessTime;
    }
```

**LogReader**
```java
    public interface LogReader<T> {
        List<T> fileRead(String filePath);
    }
```

**ApiLogMapper**
```java
    @Slf4j
    public class ApiLogMapper implements Mapper<InputApiLog> {
    
        @Override
        public InputApiLog lineMapper(String line) {
    
            // format check
            String[] logArray = line.split("]");
            if (logArray.length!=4){
                log.error("The format does not match.");
                return null;
            }
    
            // value check
            int code = 0;
            HttpStatus httpStatus;
            URL url = null;
            String aUrl = "";
            try{
                code = Integer.parseInt(logArray[0].replace("[", ""));
                httpStatus = HttpStatus.ofLegacyCode(code);
                if (httpStatus==null){
                    log.error(code + " code does not exist in httpStatus");
                    return null;
                }
                aUrl = logArray[1].replace("[", "");
                url = new URL(aUrl);
            }catch (NumberFormatException ne){
                log.error(code + " is not a numeric format");
                return null;
            } catch (MalformedURLException me){
                log.error(String.format("Invalid URL. - URL : %s", aUrl));
                return null;
            }
    
            return InputApiLog.builder()
                    .code(HttpStatus.ofLegacyCode(code))
                    .url(url)
                    .webBrowser(logArray[2].replace("[", ""))
                    .accessTime(LocalDateTime.parse(logArray[3].replace("[", ""), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .build();
        }
    }
```

### 2. 로그 분석
**OutputApiLog**
```java
    @Data
    public class OutputApiLog {
        private Map<String, Integer> apiKeyMap = new HashMap<>();
        private Map<String, Integer> serviceMap = new HashMap<>();
        private Map<String, Integer> browserMap = new HashMap<>();
        private int totalCount;
    }
```
**AnalysisServiceImpl**
```java
   @Slf4j
   public class AnalysisServiceImpl implements AnalysisService<InputApiLog, OutputApiLog> {
   
       @Override
       public OutputApiLog analysis(List<InputApiLog> inputApiLogs) {
           OutputApiLog outputApiLog = new OutputApiLog();
           outputApiLog.setTotalCount(inputApiLogs.size());
   
           for(InputApiLog inputApiLog : inputApiLogs) {
               URL url = inputApiLog.getUrl();
   
               String serviceId = getServiceId(url.getPath());
               Map<String, Integer> serviceMap = outputApiLog.getServiceMap();
   
               if(serviceMap.containsKey(serviceId)) {
                   serviceMap.put(serviceId, serviceMap.get(serviceId) + 1);
               } else {
                   serviceMap.put(serviceId, 1);
               }
   
               String apiKey = getApiKey(url.getQuery());
               Map<String, Integer> apiKeyMap = outputApiLog.getApiKeyMap();
               if(apiKeyMap.containsKey(apiKey)) {
                   apiKeyMap.put(apiKey, apiKeyMap.get(apiKey) + 1);
               } else {
                   apiKeyMap.put(apiKey, 1);
               }
   
               String browser = inputApiLog.getWebBrowser();
               Map<String, Integer> browserMap = outputApiLog.getBrowserMap();
               if(browserMap.containsKey(browser)) {
                   browserMap.put(browser, browserMap.get(browser) + 1);
               } else {
                   browserMap.put(browser, 1);
               }
           }
           outputApiLog.setApiKeyMap(SortUtils.sortByValue(outputApiLog.getApiKeyMap(), SortDirection.DESC));
           outputApiLog.setServiceMap(SortUtils.sortByValue(outputApiLog.getServiceMap(), SortDirection.DESC));
           outputApiLog.setBrowserMap(SortUtils.sortByValue(outputApiLog.getBrowserMap(), SortDirection.DESC));
           return outputApiLog;
       }
   
       private String getServiceId(String urlPath) {
   
           String[] array = urlPath.split("/");
   
           if (array.length == 0)
               return "";
   
           return array[array.length - 1];
       }
   
       private String getApiKey(String query) {
   
           if (query == null)
               return null;
   
           String[] keyArray = query.split("&");
   
           Map<String, String> apiKey = new HashMap<>();
           for (String key : keyArray) {
               String[] temp = key.split("=");
               apiKey.put(temp[0], temp[1]);
           }
   
           return apiKey.get("apikey");
       }
   }
```
## 3. 로그 출력
- WriteImpl은 파일을 저장할 경로와 OutputApiLog의 객체를 받아 출력 포맷에 맞게 문자열을 작성한 후
java.nio.file.Files.wirte 사용 하여 파일 쓰기를 한다.

```
    java
    @Slf4j
    public class WriteImpl implements Write<OutputApiLog> {
        @Override
        public void write(String filePath, OutputApiLog result) {
            StringBuilder sb = new StringBuilder();
    
            sb.append("최다 호출 API KEY\n");
            sb.append(new LinkedList<>(result.getApiKeyMap().entrySet()).get(0).getKey()).append("\n\n");
    
            sb.append("상위 3개 API Service ID와 각각의 요청 수\n");
            List<Map.Entry<String, Integer>> serviceIdList = new LinkedList<>(result.getServiceMap().entrySet());
            for(Map.Entry<String, Integer> entry: serviceIdList.subList(0, 3)){
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            sb.append("\n");
    
            sb.append("웹브라우저별 사용 비율\n");
            for(Map.Entry<String, Integer> entry: result.getBrowserMap().entrySet()){
                float value = ((float)entry.getValue()/result.getTotalCount())*100;
                sb.append(entry.getKey()).append(": ").append(String.format("%.2f", value)).append("%\n");
            }
    
            try {
                Files.write(Paths.get(filePath), Collections.singleton(sb.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
```

