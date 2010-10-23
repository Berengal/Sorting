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
public class BubbleSort implements SortingAlgorithm {
	@Override
	public void sort(MemoryArray input, MemoryManager memoryManager) {
		for (int max = input.getSize(); max > 0; max--) {
			for (int i = 0; i < max - 1; i++) {
				if (input.read(i).compareTo(input.read(i+1)) > 0) {
					input.swap(i+1, i);
				}
			}
			input.read(max -1).markSorted();
		}
	}
}