/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.karevoll.sorting.memory;

import java.util.ArrayList;
import java.util.List;

import no.karevoll.sorting.App;

/**
 * 
 * @author berengal
 */
public class MemoryManager implements IMemoryManager {

    public static MemoryManager newInstance(int maxValue) {
	return new MemoryManager(maxValue);
    }

    private final List<MemoryArray> memory = new ArrayList<MemoryArray>();
    private final int maxValue;

    private MemoryManager(int maxValue) {
	this.maxValue = maxValue;
    }

    public MemoryArray allocate(int size, String label) {
	final MemoryArray newMemory = MemoryArray.newInstance(size, maxValue,
		label);
	memory.add(newMemory);
	return newMemory;
    }

    public MemoryArray allocateShuffled(int size, String label) {
	final MemoryArray newMemory = MemoryArray.shuffledInstance(size, label);
	memory.add(newMemory);
	return newMemory;
    }

    public double getTime() {
	int r = 0;

	for (MemoryArray a : memory) {
	    r += a.getReads() * App.READ_TIME;
	    r += a.getWrites() * App.WRITE_TIME;
	    r += a.getRemoves() * App.REMOVE_TIME;
	    r += a.getSwaps() * App.SWAP_TIME;
	}

	return r / 1000.0;
    }

    public void free(MemoryArray memoryArray) {
	memory.remove(memoryArray);
    }

}
