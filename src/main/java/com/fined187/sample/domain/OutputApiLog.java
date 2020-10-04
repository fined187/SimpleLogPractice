package com.fined187.sample.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class OutputApiLog {
    private Map<String, Integer> apiKeyMap = new HashMap<>();
    private Map<String, Integer> serviceMap = new HashMap<>();
    private Map<String, Integer> browserMap = new HashMap<>();
    private int totalCount;
}
