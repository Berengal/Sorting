package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.IMemoryManager;
import no.karevoll.sorting.memory.MemoryArray;

public class InsertionSort implements SortingAlgorithm {
    @Override
    public void sort(MemoryArray input, IMemoryManager memoryManager) {
	for (int i = 1; i < input.getSize(); i++) {
	    int j = i - 1;
	    while (j >= 0 && input.read(j).compareTo(input.read(j + 1)) > 0)
		input.swap(j + 1, j--);
	}
    }
}