package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.Element;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;
import no.karevoll.sorting.memory.MemorySlice;

public class ShakerSort implements SortingAlgorithm {
    @Override
    public void sort(MemoryArray input, MemoryManager memoryManager) {
	MemorySlice memory = new MemorySlice(input);

	while (memory.getSize() > 1) {
	    int last = 0;
	    Element c = memory.remove(0);
	    for (int i = 0; i < memory.getSize() - 1; i++) {
		Element n = memory.remove(i + 1);
		if (c.compareTo(n) > 0) {
		    memory.insert(n, i);
		    last = i + 1;
		} else {
		    memory.insert(c, i);
		    c = n;
		}
	    }
	    memory.insert(c, memory.getSize() - 1);
	    memory.sliceRight(last).markSorted();
	    memory = memory.sliceLeft(last);

	    if (memory.getSize() == 0)
		return;

	    c = memory.remove(memory.getSize() - 1);
	    for (int i = memory.getSize() - 1; i > 0; i--) {
		Element n = memory.remove(i - 1);
		if (c.compareTo(n) < 0) {
		    memory.insert(n, i);
		    last = i;
		} else {
		    memory.insert(c, i);
		    c = n;
		}
	    }
	    memory.insert(c, 0);
	    memory.sliceLeft(last).markSorted();
	    memory = memory.sliceRight(last);
	}
    }
}