package com.ada.brasileirao.repository;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.*;

public class CsvParserImp implements CsvParser {
    private static final Pattern CSV_FIELD =
            Pattern.compile("\"([^\"]*(?:\"\"[^\"]*)*)\"|([^,]*)");

    @Override
    public Stream<Map<String, String>> parseLines(Stream<String> lines) {
        Objects.requireNonNull(lines, "Linhas não devem ser nulas");

        Iterator<String> it = lines.iterator();
        if (!it.hasNext()) {
            return Stream.<Map<String, String>>empty().onClose(lines::close);
        }

        String headerLine = it.next();
        List<String> headers = parseCsvLine(headerLine).stream()
                .map(String::trim)
                .toList();

        Spliterator<String> spl = Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED | Spliterator.NONNULL);
        Stream<String> remaining = StreamSupport.stream(spl, false)
                .onClose(lines::close);

        return remaining
                .map(this::parseCsvLine)
                .map(values -> toMapFunctional(headers, values))
                .onClose(lines::close);
    }

    private List<String> parseCsvLine(String line) {
        if (line == null) return Collections.emptyList();
        Matcher m = CSV_FIELD.matcher(line);
        Spliterator<String> spl = new Spliterator<>() {
            @Override
            public boolean tryAdvance(Consumer<? super String> action) {
                if (m.find()) {
                    String quoted = m.group(1);
                    String unquoted = m.group(2);
                    String field = (quoted != null)
                            ? quoted.replace("\"\"", "\"")
                            : (unquoted != null ? unquoted : "");
                    action.accept(field);
                    return true;
                }
                return false;
            }

            @Override public Spliterator<String> trySplit() { return null; }
            @Override public long estimateSize() { return Long.MAX_VALUE; }
            @Override public int characteristics() { return ORDERED | NONNULL; }
        };
        return StreamSupport.stream(spl, false).toList();
    }

    private Map<String, String> toMapFunctional(List<String> headers, List<String> values) {
        Map<String, String> map = IntStream.range(0, headers.size())
                .boxed()
                .collect(Collectors.toMap(
                        headers::get,
                        i -> (i < values.size() ? values.get(i) : ""),
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
        return Collections.unmodifiableMap(map);
    }
}
