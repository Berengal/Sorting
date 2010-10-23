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
public class InsertionSort implements SortingAlgorithm {
	@Override
	public void sort(MemoryArray input, MemoryManager memoryManager) {
		for (int i = 1; i < input.getSize(); i++) {
			int j = i-1;
			while (j >= 0 && input.read(j).compareTo(input.read(j+1)) > 0)
				input.swap(j+1, j--);
		}
	}
}

class ShellSort implements SortingAlgorithm {
	@Override
	public void sort(MemoryArray input, MemoryManager memoryManager) {
		int inc = input.getSize()/2;
		while (inc > 0) {
			for (int i = inc; i < input.getSize(); i++) {
				for (int j = i; j >= inc && input.read(j).compareTo(input.read(j-inc)) < 0; j -= inc) {
					input.swap(j, j - inc);
				}
			}
			if (inc > 2)
				inc /= 2.2;
			else
				inc--;
		}

	}
}