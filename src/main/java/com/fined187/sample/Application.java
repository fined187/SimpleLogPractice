package com.fined187.sample;

import com.fined187.sample.domain.InputApiLog;
import com.fined187.sample.domain.OutputApiLog;
import com.fined187.sample.enums.HttpStatus;
import com.fined187.sample.io.readers.Reader;
import com.fined187.sample.io.writer.Write;
import com.fined187.sample.io.writer.WriteImpl;
import com.fined187.sample.mapper.ApiLogMapper;
import com.fined187.sample.service.AnalysisService;
import com.fined187.sample.service.AnalysisServiceImpl;
import com.fined187.sample.util.ResourceUtils;
import lombok.extern.slf4j.Slf4j;


import java.net.MalformedURLException;
import java.util.List;
import com.google.common.collect.Lists;

@Slf4j
public class Application {
    public static void main(String[] args) throws MalformedURLException {
        List<InputApiLog> inputApiLogList = Lists.newArrayList();

        Reader<InputApiLog> reader = new Reader<>(new ApiLogMapper());
        reader.setFilter(v -> v.getCode() == HttpStatus.OK);
        inputApiLogList = reader.fileRead(ResourceUtils.getResource("samplelog.log"));

        AnalysisService<InputApiLog, OutputApiLog> service = new AnalysisServiceImpl();
        OutputApiLog analysisResult = service.analysis(inputApiLogList);

        Write<OutputApiLog> write = new WriteImpl();
        write.write("src/main/resources/output.log", analysisResult);

    }
}


