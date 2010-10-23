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
public class SelectionSort implements SortingAlgorithm {
	@Override
	public void sort(MemoryArray input, MemoryManager memoryManager) {
		for (int i = 0; i < input.getSize(); i++) {
			int min = i;
			Element minE = input.read(i);
			for (int j = i+1; j < input.getSize(); j++) {
				if (minE.compareTo(input.read(j)) >= 0) {
					min = j;
					minE = input.read(min);
				}
			}
			if (min != i)
				input.swap(i, min);
			minE.markSorted();
		}
	}

}