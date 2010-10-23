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
public class ShakerSort implements SortingAlgorithm {
	@Override
	public void sort(MemoryArray input, MemoryManager memoryManager) {
		int min = 0, max = input.getSize();
		boolean done = false;
		while (min < max && !done) {
			done = true;
			for (int i = min; i < max - 1; i++) {
				if (input.read(i).compareTo(input.read(i+1)) > 0) {
					input.swap(i+1, i);
					done = false;
				}
			}
			input.read(--max).markSorted();

			for (int i = max -1; i > min; i--) {
				if (input.read(i).compareTo(input.read(i-1)) < 0) {
					input.swap(i-1, i);
					done = false;
				}
			}
			input.read(min++).markSorted();
		}

		for (int i = min; i < max; i++) {
			input.read(i).markSorted();
		}
	}
}