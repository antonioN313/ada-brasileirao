package com.ada.brasileirao.io;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CsvParserImp implements CsvParser {
    private static final Pattern CSV_FIELD = Pattern.compile("\"([^\"]*(?:\"\"[^\"]*)*)\"|([^,]*)");

    @Override
    public Stream<Map<String, String>> parseLines(Stream<String> lines) {
        Objects.requireNonNull(lines, "Linhas nao devem ser nulas");

        Iterator<String> it = lines.iterator();
        if (!it.hasNext()) {

            return Stream.<Map<String,String>>empty().onClose(lines::close);
        }


        String headerLine = it.next();
        List<String> headers = parseCsvLine(headerLine).stream()
                .map(String::trim)
                .toList();


        Spliterator<String> remainingSpl = Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED | Spliterator.NONNULL);
        Stream<String> remainingLines = StreamSupport.stream(remainingSpl, false)
                .onClose(lines::close);


        return remainingLines
                .map(this::parseCsvLine)
                .map(values -> toMapFunctional(headers, values))
                .onClose(lines::close);
    }


    private Map<String, String> toMapFunctional(List<String> headers, List<String> values) {
        Map<String, String> linked = IntStream.range(0, headers.size())
                .boxed()
                .collect(Collectors.toMap(
                        headers::get,
                        i -> i < values.size() ? values.get(i) : "",
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
        return Collections.unmodifiableMap(linked);
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
                    String field = quoted != null ? quoted.replace("\"\"", "\"") : (unquoted != null ? unquoted : "");
                    action.accept(field);
                    return true;
                }
                return false;
            }

            @Override
            public Spliterator<String> trySplit() { return null; }
            @Override
            public long estimateSize() { return Long.MAX_VALUE; }
            @Override
            public int characteristics() { return ORDERED | NONNULL; }
        };

        return StreamSupport.stream(spl, false)
                .toList();
    }
}


