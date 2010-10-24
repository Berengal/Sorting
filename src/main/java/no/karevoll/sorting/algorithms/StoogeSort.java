package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;
import no.karevoll.sorting.memory.MemorySlice;

public class StoogeSort implements SortingAlgorithm {
    @Override
    public void sort(MemoryArray input, MemoryManager memoryManager) {
	MemorySlice memory = new MemorySlice(input);
	sort(memory);
	memory.markSorted();
    }

    private void sort(MemorySlice input) {
	if (input.getSize() <= 1)
	    return;

	input.compareAndSwap(0, input.getSize() - 1);

	if (input.getSize() > 2) {
	    int t = input.getSize() / 3;
	    sort(input.sliceLeft(input.getSize() - t));
	    sort(input.sliceRight(t));
	    sort(input.sliceLeft(input.getSize() - t));
	}
    }
}