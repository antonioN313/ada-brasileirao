package com.ada.brasileirao.repository.mapper;

import com.ada.brasileirao.model.Match;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class MatchMapper implements CsvToDomainMapper<Match> {
    private static final LocalDate DEFAULT_DATE = LocalDate.of(1900, 1, 1);

    private final List<DateTimeFormatter> dateFormats;

    public MatchMapper() {
        this(List.of(DateTimeFormatter.ofPattern("d/M/uuuu"), DateTimeFormatter.ISO_LOCAL_DATE));
    }

    public MatchMapper(List<DateTimeFormatter> dateFormats) {
        this.dateFormats = List.copyOf(Objects.requireNonNull(dateFormats));
    }

    @Override
    public Optional<Match> map(Map<String, String> row) {
        Objects.requireNonNull(row);

        Optional<String> homeOpt = firstNonBlank(row, "mandante", "home", "time_mandante");
        Optional<String> awayOpt = firstNonBlank(row, "visitante", "away", "time_visitante");

        return homeOpt.flatMap(home ->
                awayOpt.map(away -> {
                    LocalDate date = firstNonBlank(row, "data", "date", "data_jogo")
                            .flatMap(this::parseDate)
                            .orElse(DEFAULT_DATE);

                    int homeGoals = firstNonBlank(row, "gols_mandante", "home_goals", "mandante_gols")
                            .flatMap(this::parseInt)
                            .orElse(0);
                    int awayGoals = firstNonBlank(row, "gols_visitante", "away_goals", "visitante_gols")
                            .flatMap(this::parseInt)
                            .orElse(0);

                    String state = firstNonBlank(row, "estado", "uf", "estadio_estado")
                            .map(String::trim)
                            .orElse("UNKNOWN");

                    return new Match(date, home.trim(), away.trim(), homeGoals, awayGoals, state);
                })
        );
    }

    private static Optional<String> firstNonBlank(Map<String, String> row, String... keys) {
        for (String k : keys) {
            if (row.containsKey(k)) {
                String v = row.get(k);
                if (v != null && !v.isBlank()) {
                    return Optional.of(v);
                }
            }
        }
        return Optional.empty();
    }

    private Optional<LocalDate> parseDate(String raw) {
        if (raw == null || raw.isBlank()) return Optional.empty();
        String s = raw.trim();
        for (DateTimeFormatter fmt : dateFormats) {
            try {
                return Optional.of(LocalDate.parse(s, fmt));
            } catch (DateTimeParseException ignored) {}
        }
        try {
            return Optional.of(LocalDate.parse(s));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    private Optional<Integer> parseInt(String raw) {
        if (raw == null || raw.isBlank()) return Optional.empty();
        String cleaned = raw.trim().replaceAll("[^0-9\\-]", "");
        if (cleaned.isEmpty()) return Optional.empty();
        try {
            return Optional.of(Integer.parseInt(cleaned));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
