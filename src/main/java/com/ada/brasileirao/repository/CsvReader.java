package com.ada.brasileirao.repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface CsvReader {
    Stream<Map<String, String>> readStream(Path path);
    List<Map<String, String>> readAll(Path path);
}
