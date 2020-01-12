package org.pal.stream;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamMerger<T> implements Spliterator<T> {
    private final List<StreamPeeker<T>> streams;
    private final Comparator<T> comparator;

    @SafeVarargs
    public StreamMerger(Comparator<T> c, Stream<T>... strms) {
        comparator = c;
        streams = new LinkedList<>();
        for (Stream<T> s : strms) {
            streams.add(new StreamPeeker<>(s));
        }
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {

        T minValue = null;
        StreamPeeker<T> minStream = null;

        for (StreamPeeker<T> entry: streams) {
            if (entry.hasMore()) {
                T value = entry.peek();
                if (minValue == null
                        // value < minValue
                        || comparator.compare(value, minValue) < 0) {
                    minValue = value;
                    minStream = entry;
                }
            }
        }
        boolean result = false;
        if (minStream != null) {
            action.accept(minValue);
            minStream.next();
            result = true;
        }

        return result;
    }

    @Override
    public Spliterator<T> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        long size = 0;
        for (StreamPeeker<T> s: streams) {
            size += s.estimateSize();
        }
        return size;
    }

    @Override
    public int characteristics() {
        return Spliterator.ORDERED;
    }

    public static<T> Stream<T> merge(Comparator<T> c, Stream<T>... strms) {
        StreamMerger<T> sm = new StreamMerger<>(c, strms);
        return StreamSupport.stream(sm, false);
    }

    public static void main(String... a) {
        Stream<Integer> s1 = Stream.of(2,5, 7,11);
        Stream<Integer> s2 = Stream.of(3,5,6,13);
        Stream<Integer> s3 = Stream.of(4);

        Stream<Integer> sm = merge(
                Comparator.naturalOrder(),
                s1, s2, s3);
        sm.forEach(System.out::println);
    }
}
