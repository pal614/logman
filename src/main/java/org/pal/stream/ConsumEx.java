package org.pal.stream;

import java.util.*;

public class ConsumEx {
    public static void main(String[] a) {
        int[] ar = {1,2};
        MinDiffFinder collect =  Arrays.asList(10, 40, 500, 444, 23, 525, 334, 356, 35, 93, 32, 44, 123, 53, 130, 21)
                .parallelStream()
                .collect(MinDiffFinder::new,
                        MinDiffFinder::accum, MinDiffFinder::combine);
        System.out.println(collect.minDiff);
    }

    static class MinDiffFinder {
        int minDiff = Integer.MAX_VALUE;
        SortedSet<Integer> set = new TreeSet<>();

        void accum(Integer i) {
            if (set.isEmpty()) {
                set.add(i);
            } else {
                if (minDiff != 0) {
                    if (set.contains(i)) {
                        minDiff = 0;
                    } else {
                        SortedSet<Integer> headSet = set.headSet(i);
                        if (! headSet.isEmpty()) {
                            int headDiff = i - headSet.last();
                            if (headDiff < minDiff) {
                                minDiff = headDiff;
                            }
                        }
                        SortedSet<Integer> tailSet = set.tailSet(i);
                        if (! tailSet.isEmpty()) {
                            int tailDiff = tailSet.first() - i;
                            if (tailDiff < minDiff) {
                                minDiff = tailDiff;
                            }
                        }
                        set.add(i);
                    }
                }
            }

        }

        void combine(MinDiffFinder other) {
            if (other.minDiff == 0) {
                minDiff = 0;
            } else {
                for (Integer i: other.set) {
                    accum(i);
                }
            }
        }
    }
}
