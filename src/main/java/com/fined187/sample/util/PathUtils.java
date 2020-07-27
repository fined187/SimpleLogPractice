package com.fined187.sample.util;

import java.util.Objects;

public class PathUtils {
    private PathUtils() {}

    public static String getPathUtils(String fileName) {
        ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();
        String filePath = "";
        filePath = Objects.requireNonNull(currentThreadClassLoader.getResource(fileName)).getPath();
        return filePath;
    }
}
