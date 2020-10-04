package com.fined187.sample.domain;

import lombok.Builder;
import lombok.Getter;

import java.net.URL;
import java.time.LocalDateTime;

@Getter
@Builder
public class InputApiLog {
    private String code;
    private URL url;
    private String WebBrowser;
    private LocalDateTime accessTime;
}
