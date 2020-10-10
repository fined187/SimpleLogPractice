package com.fined187.sample.mapper;

import com.fined187.sample.domain.InputApiLog;
import com.fined187.sample.enums.HttpStatus;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ApiLogMapper implements Mapper<InputApiLog> {

    @Override
    public InputApiLog lineMapper(String line) {

        // format check
        String[] logArray = line.split("]");
        if (logArray.length!=4){
            log.error("The format does not match.");
            return null;
        }

        // value check
        int code = 0;
        HttpStatus httpStatus;
        URL url = null;
        String aUrl = "";
        try{
            code = Integer.parseInt(logArray[0].replace("[", ""));
            httpStatus = HttpStatus.ofLegacyCode(code);
            if (httpStatus==null){
                log.error(code + " code does not exist in httpStatus");
                return null;
            }
            aUrl = logArray[1].replace("[", "");
            url = new URL(aUrl);
        }catch (NumberFormatException ne){
            log.error(code + " is not a numeric format");
            return null;
        } catch (MalformedURLException me){
            log.error(String.format("Invalid URL. - URL : %s", aUrl));
            return null;
        }

        return InputApiLog.builder()
                .code(HttpStatus.ofLegacyCode(code))
                .url(url)
                .webBrowser(logArray[2].replace("[", ""))
                .accessTime(LocalDateTime.parse(logArray[3].replace("[", ""), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}