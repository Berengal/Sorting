package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.Element;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;
import no.karevoll.sorting.memory.MemorySlice;

public class MergeSort implements SortingAlgorithm {
    @Override
    public void sort(MemoryArray input, MemoryManager memoryManager) {
	MemorySlice memory = new MemorySlice(input);
	sort(memory, new MemorySlice(memoryManager.allocate(memory.getSize(),
		"Scratch")));
	memory.markSorted();
    }

    private void sort(MemorySlice memory, MemorySlice scratch) {
	if (memory.getSize() <= 1) {
	    return;
	}
	if (memory.getSize() == 2)
	    memory.compareAndSwap(0, 1);

	int p = memory.getSize() / 2;
	MemorySlice left = memory.sliceLeft(p);
	MemorySlice right = memory.sliceRight(p);
	sort(left, scratch.sliceLeft(p));
	sort(right, scratch.sliceRight(p));

	int i = 0, j = 0;

	boolean done = false;
	Element leftElement = left.remove(0);
	Element rightElement = right.remove(0);
	while (!done) {
	    if (leftElement.compareTo(rightElement) < 0) {
		scratch.insert(leftElement, i++ + j);
		if (i < left.getSize()) {
		    leftElement = left.remove(i);
		} else {
		    scratch.insert(rightElement, i + j++);
		    done = true;
		}
	    } else {
		scratch.insert(rightElement, i + j++);
		if (j < right.getSize()) {
		    rightElement = right.remove(j);
		} else {
		    scratch.insert(leftElement, i++ + j);
		    done = true;
		}
	    }
	}

	while (i < left.getSize()) {
	    scratch.insert(left.remove(i), i + j);
	    i++;
	}

	while (j < right.getSize()) {
	    scratch.insert(right.remove(j), i + j);
	    j++;
	}

	for (i = 0; i < scratch.getSize(); i++) {
	    Element e = scratch.read(i);
	    scratch.markRemoved(i);
	    memory.insert(e, i);
	}
    }
}