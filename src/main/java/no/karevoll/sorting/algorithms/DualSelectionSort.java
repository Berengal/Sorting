package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.Element;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;
import no.karevoll.sorting.memory.MemorySlice;

public class DualSelectionSort implements SortingAlgorithm {
    @Override
    public void sort(MemoryArray input, MemoryManager memoryManager) {
	MemorySlice memory = new MemorySlice(input);
	for (int i = 0, j = memory.getSize() - 1; i < j; i++) {
	    Element min = memory.read(i);
	    int minIndex;
	    Element max = memory.read(i + 1);
	    int maxIndex;

	    if (min.compareTo(max) > 0) {
		Element e = max;
		max = min;
		min = e;
		minIndex = i + 1;
		maxIndex = i;
	    } else {
		minIndex = i;
		maxIndex = i + 1;
	    }

	    for (int c = i + 2; c < j; c += 2) {
		Element a = memory.read(c);
		Element b = memory.read(c + 1);
		if (a.compareTo(b) < 0) {
		    if (a.compareTo(min) < 0) {
			min = a;
			minIndex = c;
		    }
		    if (b.compareTo(max) > 0) {
			max = b;
			maxIndex = c + 1;
		    }
		} else {
		    if (a.compareTo(max) > 0) {
			max = a;
			maxIndex = c;
		    }
		    if (b.compareTo(min) < 0) {
			min = b;
			minIndex = c + 1;
		    }
		}
	    }

	    if ((j - i) % 2 == 0) {
		Element e = memory.read(j);
		if (e.compareTo(min) < 0) {
		    min = e;
		    minIndex = j;
		} else if (e.compareTo(max) > 0) {
		    max = e;
		    maxIndex = j;
		}
	    }

	    if (minIndex != i) {
		memory.set(memory.remove(i), minIndex);
		memory.insert(min, i);
		if (maxIndex == i)
		    maxIndex = minIndex;
	    }
	    min.markSorted();

	    if (maxIndex != j) {
		memory.set(memory.remove(j), maxIndex);
		memory.insert(max, j);
	    }
	    max.markSorted();
	    j--;
	}
    }

}