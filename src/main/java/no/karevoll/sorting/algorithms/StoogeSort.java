package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;

public class StoogeSort implements SortingAlgorithm {
    @Override
    public void sort(MemoryArray input, MemoryManager memoryManager) {
	sort(input, 0, input.getSize());
    }

    private void sort(MemoryArray input, final int start, final int end) {
	if (start >= end)
	    return;

	if (input.read(start).compareTo(input.read(end - 1)) > 0) {
	    input.swap(start, end - 1);
	}

	if (end - start > 2) {
	    int t = (end - start) / 3;
	    sort(input, start, end - t);
	    sort(input, start + t, end);
	    sort(input, start, end - t);
	}
    }
}