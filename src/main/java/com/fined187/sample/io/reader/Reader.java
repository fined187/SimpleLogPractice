package com.fined187.sample.io.reader;

import com.fined187.sample.mapper.Mapper;
import com.sun.tools.javac.util.StringUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class Reader<T> implements LogReader<T>{

    private Mapper<T> mapper;

    @Setter
    Predicate<T> filter;

    public Reader(Mapper<T> mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<T> fileRead(String filePath) {
        List<T> list = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
                list = stream.filter(StringUtils::isNoneBlank)
                        .map(v -> mapper.lineMapper(v))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                if(filter != null) {
                    list = list.stream().filter(filter).collect(Collectors.toList());
                }

        } catch (IOException e) {
            log.error(String.format("File Read Exception. %s", filePath));
            e.printStackTrace();
        }
        return list;
    }
}
