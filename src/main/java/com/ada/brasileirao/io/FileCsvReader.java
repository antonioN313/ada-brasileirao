package com.ada.brasileirao.io;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

public class FileCsvReader implements CsvReader {

    private final CsvParser parser;

    public FileCsvReader(CsvParser parser) {
        this.parser = parser;
    }

    public Stream<Map<String, String>> readStream(Path path) {
        try {
            Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8);
            Stream<Map<String, String>> rows = parser.parseLines(lines);
            return rows.onClose(() -> {
                try { lines.close(); } catch (RuntimeException ex) { throw ex; }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public java.util.List<Map<String,String>> readAll(Path path) {
        try (Stream<Map<String,String>> s = readStream(path)) {
            return s.toList();
        }
    }
}
