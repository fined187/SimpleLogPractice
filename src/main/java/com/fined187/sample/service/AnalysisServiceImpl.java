package com.fined187.sample.service;

import com.fined187.sample.domain.InputApiLog;
import com.fined187.sample.domain.OutputApiLog;
import com.fined187.sample.enums.SortDirection;
import com.fined187.sample.util.SortUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
