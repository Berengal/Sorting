/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.IMemoryManager;
import no.karevoll.sorting.memory.MemoryArray;

/**
 * 
 * @author berengal
 */
public class HeapSort implements SortingAlgorithm {
    @Override
    public void sort(MemoryArray input, IMemoryManager memoryManager) {
	for (int i = input.getSize() / 2; i >= 0; i--) {
	    downheap(input, input.getSize(), i);
	}

	for (int i = input.getSize() - 1; i > 0; i--) {
	    input.swap(0, i);
	    input.read(i).markSorted();
	    downheap(input, i, 0);
	}
	input.read(0).markSorted();
    }

    private void downheap(MemoryArray input, int size, int i) {
	if ((i + 1) * 2 - 1 >= size)
	    return;

	int t;
	if ((i + 1) * 2 >= size
		|| input.read((i + 1) * 2 - 1).compareTo(
			input.read((i + 1) * 2)) > 0)
	    t = (i + 1) * 2 - 1;
	else
	    t = (i + 1) * 2;

	if (input.read(t).compareTo(input.read(i)) > 0) {
	    input.swap(i, t);
	    downheap(input, size, t);
	}
    }
}