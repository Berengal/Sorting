package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;
import no.karevoll.sorting.memory.MemorySlice;

public class BubbleSort implements SortingAlgorithm {
    @Override
    public void sort(MemoryArray input, MemoryManager memoryManager) {
	MemorySlice memory = new MemorySlice(input);
	for (int max = memory.getSize(); max > 0; max--) {
	    for (int i = 0; i < max - 1; i++) {
		memory.compareAndSwap(i, i + 1);
	    }
	    memory.read(max - 1).markSorted();
	}
    }
}