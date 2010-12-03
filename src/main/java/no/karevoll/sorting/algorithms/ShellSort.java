package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.Element;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;
import no.karevoll.sorting.memory.MemorySlice;

public class ShellSort implements SortingAlgorithm {
    @Override
    public void sort(MemoryArray input, MemoryManager memoryManager) {
        MemorySlice memory = new MemorySlice(input);
        int inc = memory.getSize() / 2;
        while (inc > 0) {
            for (int i = inc; i < memory.getSize(); i++) {
                Element c = memory.remove(i);
                boolean done = false;
                for (int j = i - inc; j >= 0 && !done; j -= inc) {
                    Element n = memory.read(j);
                    if (c.compareTo(n) < 0) {
                        memory.insert(n, j + inc);
                        memory.markRemoved(j);
                    } else {
                        memory.insert(c, j + inc);
                        done = true;
                    }
                }
                if (!done) {
                    memory.insert(c, i % inc);
                }
            }
            if (inc > 2)
                inc /= 2.2;
            else
                inc--;
        }

        memory.markSorted();
    }
}