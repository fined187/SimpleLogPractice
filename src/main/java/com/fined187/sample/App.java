package com.fined187.sample;

import com.fined187.sample.reader.LogReader;

import java.util.List;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {
        LogReader reader = new LogReader();
        List<String> logList = reader.fileReader("samplelog.log");
        List<String> logAnalysis = logList.stream()
                .filter(la -> la.contains("[200]"))
                .map(la -> la.toString())
                .collect(Collectors.toList());
//        logAnalysis.forEach(System.out::println);

        List<String> webList = logAnalysis.stream()
                .filter(wl -> wl.contains("IE"))
                .map(wl -> wl.toString())
                .collect(Collectors.toList());

         webList.replaceAll(l -> l.replaceAll("]", "/").replace("[", " "));

//         webList.forEach(System.out::println);
    }
}
