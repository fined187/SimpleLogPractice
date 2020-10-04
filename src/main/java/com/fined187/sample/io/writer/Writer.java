package com.fined187.sample.io.writer;

public interface Writer<T> {
    void write(String filePath, T object);
}
