package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;
import no.karevoll.sorting.memory.MemorySlice;

public class ShakerSort implements SortingAlgorithm {
    @Override
    public void sort(MemoryArray input, MemoryManager memoryManager) {
	MemorySlice memory = new MemorySlice(input);

	while (memory.getSize() > 1) {
	    int last = 0;
	    for (int i = 0; i < memory.getSize() - 1; i++) {
		if (memory.compareAndSwap(i, i + 1)) {
		    last = i + 1;
		}
	    }
	    memory.sliceRight(last).markSorted();
	    memory = memory.sliceLeft(last);

	    for (int i = memory.getSize() - 1; i > 0; i--) {
		if (memory.compareAndSwap(i - 1, i)) {
		    last = i;
		}
	    }
	    memory.sliceLeft(last).markSorted();
	    memory = memory.sliceRight(last);
	}
    }
}