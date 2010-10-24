/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;

/**
 * 
 * @author berengal
 */
public class MergeSort implements SortingAlgorithm {
    @Override
    public void sort(MemoryArray input, MemoryManager memoryManager) {
	sort(input, memoryManager, 0, input.getSize());
    }

    private void sort(MemoryArray input, MemoryManager manager,
	    final int start, final int end) {
	if (start >= end - 1)
	    return;

	int p = (start + end) / 2;
	sort(input, manager, start, p);
	sort(input, manager, p, end);

	MemoryArray A = manager.allocate(p - start, "A");
	MemoryArray B = manager.allocate(end - p, "B");

	for (int i = 0; i < A.getSize(); i++) {
	    A.insert(input.remove(start + i), i);
	}
	for (int i = 0; i < B.getSize(); i++) {
	    B.insert(input.remove(p + i), i);
	}

	int i = 0, j = 0;

	while (i < A.getSize() && j < B.getSize()) {
	    if (A.read(i).compareTo(B.read(j)) < 0) {
		input.insert(A.remove(i), start + i + j);
		i++;
	    } else {
		input.insert(B.remove(j), start + i + j);
		j++;
	    }
	}

	while (i < A.getSize()) {
	    input.insert(A.remove(i), start + i + j);
	    i++;
	}
	while (j < B.getSize()) {
	    input.insert(B.remove(j), start + i + j);
	    j++;
	}

	manager.free(A);
	manager.free(B);
    }
}