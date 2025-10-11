package com.ada.brasileirao.repository.mapper;

import java.util.Map;
import java.util.Optional;

public interface CsvToDomainMapper<T> {
    Optional<T> map(Map<String, String> row);
}
