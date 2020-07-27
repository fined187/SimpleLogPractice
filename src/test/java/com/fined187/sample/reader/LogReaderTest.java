package com.fined187.sample.reader;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogReaderTest {

    @Test
    void readerTest() throws Exception {
        //given
        LogReader reader = new LogReader();
        //when
        List<String> strings = reader.fileReader( "samplelog.log");
        //then
        strings.forEach(System.out::println);
    }
}