package com.fined187.sample.service;

import java.util.List;

public interface AnalysisService<T, O> {
    O analysis(List<T> logs);
}
