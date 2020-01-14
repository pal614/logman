package org.pal.stream;

import java.util.Comparator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StreamMergerTest {

    @org.junit.jupiter.api.Test
    void merge() {

        Stream<Integer> s1 = Stream.of(2,5, 7,11);
        Stream<Integer> s2 = Stream.of(3,5,6,13);
        Stream<Integer> s3 = Stream.of(4);

        Stream<Integer> sm = StreamMerger.merge(
                Comparator.naturalOrder(),
                s1, s2, s3);
        assertArrayEquals(sm.toArray(), new Integer[] {2,3, 4, 5, 5,  6, 7, 11, 13});

    }
}
