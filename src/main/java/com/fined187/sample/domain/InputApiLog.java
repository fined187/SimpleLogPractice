package com.fined187.sample.domain;

import com.fined187.sample.enums.HttpStatus;
import lombok.Builder;
import lombok.Getter;

import java.net.URL;
import java.time.LocalDateTime;

@Getter
@Builder
public class InputApiLog {
    private HttpStatus code;
    private URL url;
    private String webBrowser;
    private LocalDateTime accessTime;
}
