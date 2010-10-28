/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.Element;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;
import no.karevoll.sorting.memory.MemorySlice;

/**
 * 
 * @author berengal
 */
public class FasterMergeSort implements SortingAlgorithm {

    @Override
    public void sort(MemoryArray input, MemoryManager memoryManager) {
	MemorySlice memory = new MemorySlice(input);
	MemoryArray scratchMemory = memoryManager.allocate(input.getSize(),
		"Scratch memory");
	MemorySlice scratch = new MemorySlice(scratchMemory);
	sort(memory, scratch, false);
	memoryManager.free(scratchMemory);
	memory.markSorted();
    }

    private void sort(MemorySlice input, MemorySlice scratch, boolean toScratch) {
	if (input.getSize() <= 1) {
	    if (toScratch) {
		move(input, scratch);
	    }
	    return;
	} else {
	    int middle = input.getSize() / 2;

	    MemorySlice left, right, leftScratch, rightScratch;
	    left = input.sliceLeft(middle);
	    right = input.sliceRight(middle);
	    leftScratch = scratch.sliceLeft(middle);
	    rightScratch = scratch.sliceRight(middle);

	    sort(left, leftScratch, !toScratch);
	    sort(right, rightScratch, !toScratch);

	    if (toScratch) {
		merge(left, right, scratch);
	    } else {
		merge(leftScratch, rightScratch, input);
	    }
	}
    }

    private void merge(MemorySlice left, MemorySlice right, MemorySlice to) {
	int i = 0, j = 0;

	while (i < left.getSize() && j < right.getSize()) {
	    Element leftElem = left.read(i), rightElem = right.read(j);
	    if (leftElem.compareTo(rightElem) < 0) {
		left.markRemoved(i);
		to.insert(leftElem, i + j);
		i++;
	    } else {
		right.markRemoved(j);
		to.insert(rightElem, i + j);
		j++;
	    }
	}
	while (i < left.getSize()) {
	    to.insert(left.remove(i), i + j);
	    i++;
	}
	while (j < right.getSize()) {
	    to.insert(right.remove(j), i + j);
	    j++;
	}
    }

    private void move(MemorySlice from, MemorySlice to) {
	for (int i = 0; i < from.getSize(); i++) {
	    to.insert(from.remove(i), i);
	}
    }
}
