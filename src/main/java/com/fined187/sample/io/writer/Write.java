package com.fined187.sample.io.writer;

public interface Write<T> {
    void write(String filePath, T object);
}
