/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.karevoll.sorting.memory;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author berengal
 */
public class MemoryManager {

	public static MemoryManager newInstance(int maxValue) {
		return new MemoryManager(maxValue);
	}

	private final List<MemoryArray> memory = new ArrayList<MemoryArray>();
	private final int maxValue;

	private MemoryManager(int maxValue) {
		this.maxValue = maxValue;
	}

	public MemoryArray allocate(int size, String label) {
		final MemoryArray newMemory = MemoryArray.newInstance(size,
				maxValue, label);
		memory.add(newMemory);
		return newMemory;
	}

	public void free(MemoryArray memoryArray) {
		memory.remove(memoryArray);
	}

}
