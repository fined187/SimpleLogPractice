package com.fined187.sample.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class ResourceUtils {

    private ResourceUtils() {}

    public static String getResource(String fileName){
        ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();
        String filePath = "";
        try {
            filePath = Objects.requireNonNull(currentThreadClassLoader.getResource(fileName)).getPath();
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(String.format("The file does not exist in classpath. fileName : %s", fileName));
            System.exit(0);
        }
        return filePath;
    }
}
