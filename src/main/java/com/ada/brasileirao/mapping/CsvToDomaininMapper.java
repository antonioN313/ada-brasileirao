package com.ada.brasileirao.mapping;

import java.util.Map;
import java.util.Optional;

public interface CsvToDomaininMapper<T> {
    Optional<T> map(Map<String, String> row);
}
