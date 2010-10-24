/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.Element;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;

/**
 * 
 * @author berengal
 */
public class QuickSort implements SortingAlgorithm {
    @Override
    public void sort(MemoryArray input, MemoryManager memoryManager) {
	sort(input, 0, input.getSize());
    }

    private void sort(MemoryArray input, final int start, final int end) {
	if (start >= end)
	    return;
	Element pivot = input.remove(start);

	int spot = start;
	int direction = -1;

	int min = start + 1;
	int max = end - 1;
	int index = max;
	while (index != spot) {
	    if (input.read(index).compareTo(pivot) == direction) {
		input.insert(input.remove(index), spot);
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
	input.insert(pivot, spot);
	pivot.markSorted();
	sort(input, start, spot);
	sort(input, spot + 1, end);
    }

}