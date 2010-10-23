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
public class StoogeSort implements SortingAlgorithm {
    @Override
    public void sort(MemoryArray input, IMemoryManager memoryManager) {
	sort(input, 0, input.getSize());
    }

    private void sort(MemoryArray input, final int start, final int end) {
	if (start >= end)
	    return;

	if (input.read(start).compareTo(input.read(end - 1)) > 0) {
	    input.swap(start, end - 1);
	}

	if (end - start > 2) {
	    int t = (end - start) / 3;
	    sort(input, start, end - t);
	    sort(input, start + t, end);
	    sort(input, start, end - t);
	}
    }
}