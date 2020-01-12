package org.pal.stream;

import java.util.Spliterator;
import java.util.stream.Stream;

public class StreamPeeker<T>  {

    private final Spliterator<T> spliterator;
    private T currentValue;
    private boolean saved;
    private boolean hasMore;

    public StreamPeeker(Stream<T> stream) {
        this.spliterator = stream.spliterator();
        saved = false;
        hasMore = true;
    }

    public boolean hasMore() {
        if (!saved) {
            hasMore = spliterator.tryAdvance((value) -> this.currentValue = value);
            saved = true;
        }
        return hasMore;
    }

    public T peek() {
        hasMore();
        return currentValue;
    }

    public void next() {
        currentValue = null;
        saved = false;
    }
    public long estimateSize() {
        return spliterator.estimateSize() + (currentValue == null? 0 : 1);
    }
}
