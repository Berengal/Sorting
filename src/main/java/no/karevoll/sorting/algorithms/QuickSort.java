package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.Element;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;
import no.karevoll.sorting.memory.MemorySlice;

public class QuickSort implements SortingAlgorithm {
    @Override
    public void sort(MemoryArray input, MemoryManager memoryManager) {
	sort(new MemorySlice(input));
    }

    private void sort(MemorySlice memory) {
	switch (memory.getSize()) {
	case 0:
	    return;
	case 1:
	    memory.markSorted(0);
	    return;
	case 2:
	    Element a = memory.read(0);
	    Element b = memory.read(1);

	    if (a.compareTo(b) > 0) {
		memory.set(a, 1);
		memory.set(b, 0);
	    }
	    a.markSorted();
	    b.markSorted();
	    return;
	default:
	}

	int spot = partition(memory, 0);

	sort(memory.sliceLeft(spot));
	sort(memory.sliceRight(spot + 1));
    }

    public static int partition(MemorySlice memory, int pivotIndex) {
	Element pivot = memory.remove(pivotIndex);
	if (pivotIndex != 0) {
	    memory.insert(memory.remove(0), pivotIndex);
	}

	int spot = 0;
	int direction = -1;

	int min = 0;
	int max = memory.getSize() - 1;
	int index = max;
	while (index != spot) {
	    Element c = memory.read(index);
	    if (c.compareTo(pivot) == direction) {
		memory.markRemoved(index);
		memory.insert(c, spot);
		spot = index;
		if (direction == -1) {
		    max = index - 1;
		    index = min;
		} else {
		    min = index + 1;
		    index = max;
		}
		direction *= -1;
	    } else {
		index += direction;
	    }
	}

	memory.insert(pivot, spot);
	pivot.markSorted();

	return spot;
    }
}