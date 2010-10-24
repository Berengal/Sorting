package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.Element;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;
import no.karevoll.sorting.memory.MemorySlice;

public class SelectionSort implements SortingAlgorithm {
    @Override
    public void sort(MemoryArray input, MemoryManager memoryManager) {
	MemorySlice memory = new MemorySlice(input);
	for (int i = 0; i < memory.getSize(); i++) {
	    int min = i;
	    Element minE = memory.read(i);
	    for (int j = i + 1; j < memory.getSize(); j++) {
		if (minE.compareTo(memory.read(j)) >= 0) {
		    min = j;
		    minE = memory.read(min);
		}
	    }

	    memory.swap(i, min);
	    minE.markSorted();
	}
    }

}