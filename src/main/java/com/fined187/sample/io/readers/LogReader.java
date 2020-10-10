package com.fined187.sample.io.readers;

import java.util.List;

public interface LogReader<T> {
    List<T> fileRead(String filePath);
}
