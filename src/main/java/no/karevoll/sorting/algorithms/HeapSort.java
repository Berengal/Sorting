package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.Element;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;
import no.karevoll.sorting.memory.MemorySlice;

public class HeapSort implements SortingAlgorithm {
    @Override
    public void sort(MemoryArray input, MemoryManager memoryManager) {
        MemorySlice memory = new MemorySlice(input);

        for (int i = memory.getSize() / 2; i >= 0; i--) {
            downheap(memory, i);
        }

        for (int i = input.getSize() - 1; i > 0; i--) {
            Element c = memory.remove(0);
            c.markSorted();
            Element e = memory.remove(i);
            memory.insert(c, i);
            downheap(memory.sliceLeft(i), 0, e);
        }
        input.read(0).markSorted();
    }

    private void downheap(MemorySlice memory, int i, Element e) {
        int leftIndex = (i + 1) * 2 - 1;
        int rightIndex = (i + 1) * 2;
        if (leftIndex >= memory.getSize()) {
            memory.insert(e, i);
            return;
        }

        Element max = memory.read(leftIndex);
        int maxIndex = leftIndex;
        if (rightIndex < memory.getSize()) {
            Element right = memory.read(rightIndex);
            if (max.compareTo(right) < 0) {
                max = right;
                maxIndex = rightIndex;
            }
        }

        if (e.compareTo(max) < 0) {
            memory.insert(max, i);
            memory.markRemoved(maxIndex);
            downheap(memory, maxIndex, e);
        } else {
            memory.insert(e, i);
        }
    }

    private void downheap(MemorySlice memory, int i) {
        int leftIndex = (i + 1) * 2 - 1;
        int rightIndex = (i + 1) * 2;
        if (leftIndex >= memory.getSize()) {
            return;
        }

        Element max = memory.read(leftIndex);
        int maxIndex = leftIndex;
        if (rightIndex < memory.getSize()) {
            Element right = memory.read(rightIndex);
            if (max.compareTo(right) < 0) {
                max = right;
                maxIndex = rightIndex;
            }
        }

        Element e = memory.read(i);
        if (e.compareTo(max) < 0) {
            memory.set(max, i);
            memory.markRemoved(maxIndex);
            downheap(memory, maxIndex, e);
        }
    }
}