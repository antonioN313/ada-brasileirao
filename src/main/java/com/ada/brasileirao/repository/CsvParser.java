package com.ada.brasileirao.repository;

import java.util.Map;
import java.util.stream.Stream;

public interface CsvParser {
    Stream<Map<String, String>> parseLines(Stream<String> lines);
}
