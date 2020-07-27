package com.fined187.sample.reader;

import com.fined187.sample.util.PathUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LogReader
{

    public List<String> fileReader(String fileName) {
        List<String> result = new ArrayList<>();
        String filePath = PathUtils.getPathUtils(fileName);
        try {
            result = Files.lines(Paths.get(filePath))
            .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
