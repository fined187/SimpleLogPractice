package com.fined187.sample.service;

import com.fined187.sample.domain.InputLog;
import com.fined187.sample.util.PathUtils;


import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InputLogService {

    public InputLog getInputLog(String fileName) {

//        파일을 읽어서 String, Buffer --> InputLog 변환.

        String filePath = PathUtils.getPathUtils("samplelog.log");
        try {
            Files.lines(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
