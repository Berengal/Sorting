package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.IMemoryManager;
import no.karevoll.sorting.memory.MemoryArray;

public class ShellSort implements SortingAlgorithm {
    @Override
    public void sort(MemoryArray input, IMemoryManager memoryManager) {
	int inc = input.getSize() / 2;
	while (inc > 0) {
	    for (int i = inc; i < input.getSize(); i++) {
		for (int j = i; j >= inc
			&& input.read(j).compareTo(input.read(j - inc)) < 0; j -= inc) {
		    input.swap(j, j - inc);
		}
	    }
	    if (inc > 2)
		inc /= 2.2;
	    else
		inc--;
	}

    }
}