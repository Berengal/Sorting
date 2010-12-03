package no.karevoll.sorting.algorithms;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.Element;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;
import no.karevoll.sorting.memory.MemorySlice;

public class SuperParallellQuickSort implements SortingAlgorithm {
    private ExecutorService executor;
    private int             threads;

    private int cutoff() {
        return c.n / threads;
    }

    private Counter c;

    public SuperParallellQuickSort(int threads, int cutoff) {
        this.threads = threads;
    }

    @Override
    public void sort(MemoryArray input, MemoryManager memoryManager) {
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
        c = new Counter(input.getSize());
        executor = new ThreadPoolExecutor(threads, threads, 1,
                                          TimeUnit.SECONDS, queue);
        sort(new MemorySlice(input), c);
        c.waitZero();
    }

    private void sort(final MemorySlice memory, final Counter c) {
        switch (memory.getSize()) {
        case 0:
            return;
        case 1:
            memory.markSorted(0);
            c.dec();
            return;
        case 2:
            Element a = memory.read(0);
            Element b = memory.read(1);

            if (a.compareTo(b) > 0) {
                memory.set(a, 1);
                memory.set(b, 0);
            }
            a.markSorted();
            c.dec();
            b.markSorted();
            c.dec();
            return;
        default:
        }

        Element pivot = memory.remove(0);
        final MemorySlice part = memory;
        Partitioner p = new Partitioner(memory.sliceRight(1), pivot) {
            public void done(int r) {
                pivot.markSorted();
                final int spot = r + 1;
                if (spot != 1) {
                    Element t = part.remove(spot - 1);
                    part.insert(pivot, spot - 1);
                    part.insert(t, 0);
                } else {
                    part.insert(pivot, 0);
                }

                c.dec();

                executor.execute(new Runnable() {

                    public void run() {
                        sort(part.sliceLeft(spot - 1), c);
                    }

                });
                executor.execute(new Runnable() {

                    public void run() {
                        sort(part.sliceRight(spot), c);
                    }

                });
            }
        };
    }

    private void move(MemorySlice memory, int from, int to, Runnable done) {
        int len = to - from;
        int n = len / cutoff() + 1;

        Counter c = new Counter(n, done);

        for (int i = 0; i < n; i++) {
            executor.execute(new Mover(memory, from + (i * len) / n, from + ((i + 1) * len) / n, c));
        }
    }

    class Mover implements Runnable {

        MemorySlice memory;
        int         from, to;
        Counter     c;

        public Mover(MemorySlice memory, int from, int to, Counter c) {
            this.memory = memory;
            this.from = from;
            this.to = to;
            this.c = c;
        }

        @Override
        public void run() {
            for (int i = from; i < to; i++) {
                memory.swap(i, memory.getSize() - i - 1);
            }
            c.dec();
        }

    }

    private static int min(int i, int j) {
        if (i < j)
            return i;
        else
            return j;
    }

    public static int partition(MemorySlice memory, Element pivot) {
        Element last = memory.remove(0);

        int spot = 0;
        int direction = -1;

        int min = 0;
        int max = memory.getSize() - 1;
        int index = max;
        while (index != spot) {
            Element c = memory.read(index);
            if (c.compareTo(pivot) == direction) {
                memory.markRemoved(index);
                memory.insert(c, spot);
                spot = index;
                if (direction == -1) {
                    max = index - 1;
                    index = min;
                } else {
                    min = index + 1;
                    index = max;
                }
                direction *= -1;
            } else {
                index += direction;
            }
        }

        memory.insert(last, spot);
        if (last.compareTo(pivot) < 0)
            return spot + 1;
        else
            return spot;
    }

    class Partitioner implements Runnable {
        boolean     done = false;
        boolean     isLeft;
        Partitioner parent;
        int         low;
        int         high;
        int         p;
        MemorySlice memory;
        Element     pivot;

        public Partitioner(MemorySlice memory, Element pivot) {
            this(memory, pivot, false, null);
        }

        public Partitioner(MemorySlice memory, Element pivot, boolean left, Partitioner parent) {
            this.memory = memory;
            this.pivot = pivot;
            this.isLeft = left;
            this.parent = parent;
            this.p = memory.getSize() / 2;

            if (memory.getSize() < cutoff()) {
                executor.execute(this);
            } else {
                new Partitioner(memory.sliceLeft(p), pivot, true, this);
                new Partitioner(memory.sliceRight(p), pivot, false, this);
            }
        }

        private void done(int r, boolean left) {
            if (left) {
                low = r;
            } else {
                high = r + p;
            }

            if (done)
                move(memory.slice(low, high), 0, min(p - low, high - p), this);
            else
                done = true;
        }

        @Override
        public void run() {
            int r;
            if (done) {
                r = high - p + low;
            } else
                r = partition(memory, pivot);

            done(r);
        }

        void done(int r) {
            parent.done(r, isLeft);
        }
    }

    class Counter {

        private int      n;
        private Runnable done;

        public Counter(int n) {
            this.n = n;
            this.done = null;
        }

        public Counter(int n, Runnable done) {
            this.n = n;
            this.done = done;
        }

        synchronized void waitZero() {
            while (n > 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        synchronized void dec() {
            n--;

            if (n == 0) {
                if (done == null)
                    notifyAll();
                else {
                    executor.execute(done);
                }
            }
        }

    }
}
