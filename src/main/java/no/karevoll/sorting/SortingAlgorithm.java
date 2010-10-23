/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.karevoll.sorting;

import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;

/**
 *
 * @author berengal
 */
public interface SortingAlgorithm {

	void sort(MemoryArray input, MemoryManager memoryManager);

}
