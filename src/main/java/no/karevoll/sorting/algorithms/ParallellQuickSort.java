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

public class ParallellQuickSort implements SortingAlgorithm {

  private ExecutorService executor;
  private int threads;
  public ParallellQuickSort(int threads) {
    this.threads = threads;
  }

  @Override
  public void sort(MemoryArray input, MemoryManager memoryManager) {
    BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
    Counter c = new Counter(input.getSize());
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

    int spot = QuickSort.partition(memory, 0);

    c.dec();
    final int firstSpot = spot;
    if (memory.getSize() > 0) {
      executor.execute(new Runnable() {

        public void run() {
          sort(memory.sliceLeft(firstSpot), c);
        }

      });
      executor.execute(new Runnable() {

        public void run() {
          sort(memory.sliceRight(firstSpot + 1), c);
        }

      });
    } else {
      sort(memory.sliceLeft(firstSpot), c);
      sort(memory.sliceRight(firstSpot + 1), c);
    }
  }

  class Counter {

    private int n;
    public Counter(int n) {
      this.n = n;
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
      notifyAll();
    }

  }
}
