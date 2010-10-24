package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.Element;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;
import no.karevoll.sorting.memory.MemorySlice;

public class InsertionSort implements SortingAlgorithm {
    @Override
    public void sort(MemoryArray input, MemoryManager memoryManager) {
	MemorySlice memory = new MemorySlice(input);

	for (int i = 1; i < memory.getSize() - 1; i++) {
	    Element c = memory.remove(i);
	    boolean done = false;
	    for (int j = i - 1; j >= 0 && !done; j--) {
		Element n = memory.read(j);
		if (c.compareTo(n) < 0) {
		    memory.insert(n, j + 1);
		    memory.markRemoved(j);
		} else {
		    memory.insert(c, j + 1);
		    done = true;
		}
	    }
	    if (!done) {
		memory.insert(c, 0);
	    }
	}

	memory.markSorted();
    }
}