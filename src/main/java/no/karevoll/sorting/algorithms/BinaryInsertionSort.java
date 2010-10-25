package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.Element;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;
import no.karevoll.sorting.memory.MemorySlice;

public class BinaryInsertionSort implements SortingAlgorithm {
    @Override
    public void sort(MemoryArray input, MemoryManager memoryManager) {
	MemorySlice memory = new MemorySlice(input);

	for (int i = 1; i < memory.getSize(); i++) {
	    Element c = memory.remove(i);
	    insert(memory.sliceLeft(i + 1), c, binarySearch(
		    memory.sliceLeft(i), c));
	}

	memory.markSorted();
    }

    private static void insert(MemorySlice memory, Element e, int index) {
	for (int i = memory.getSize() - 1; i > index; i--) {
	    memory.insert(memory.remove(i - 1), i);
	}
	memory.insert(e, index);
    }

    public static int binarySearch(MemorySlice memory, Element e) {
	int r = 0;

	while (memory.getSize() > 0) {
	    int middle = (memory.getSize()) / 2;

	    int cmp = memory.compare(middle, e);
	    switch (cmp) {
	    case 1:
		memory = memory.sliceLeft(middle);
		break;
	    case -1:
		r += middle + 1;
		memory = memory.sliceRight(middle + 1);
		break;
	    default:
		r += middle;
		memory = memory.sliceLeft(0);
	    }
	}
	return r;
    }
}