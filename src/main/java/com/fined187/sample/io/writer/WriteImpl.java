package com.fined187.sample.io.writer;

import com.fined187.sample.domain.OutputApiLog;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
