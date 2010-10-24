package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.Element;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;
import no.karevoll.sorting.memory.MemorySlice;

public class BubbleSort implements SortingAlgorithm {
    @Override
    public void sort(MemoryArray input, MemoryManager memoryManager) {
	MemorySlice memory = new MemorySlice(input);

	for (int max = memory.getSize(); max > 0; max--) {
	    Element c = memory.remove(0);
	    for (int i = 0; i < max - 1; i++) {
		Element n = memory.remove(i + 1);
		if (c.compareTo(n) > 0) {
		    memory.insert(n, i);
		} else {
		    memory.insert(c, i);
		    c = n;
		}
	    }
	    memory.insert(c, max - 1);
	    c.markSorted();
	}
    }
}