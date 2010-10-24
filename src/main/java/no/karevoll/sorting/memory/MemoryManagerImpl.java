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
public class MemoryManagerImpl implements MemoryManager {

    public static MemoryManagerImpl newInstance(int maxValue) {
	return new MemoryManagerImpl(maxValue);
    }

    private final List<MemoryArrayImpl> memory = new ArrayList<MemoryArrayImpl>();
    private final int maxValue;

    private MemoryManagerImpl(int maxValue) {
	this.maxValue = maxValue;
    }

    public MemoryArray allocate(int size, String label) {
	final MemoryArrayImpl newMemory = MemoryArrayImpl.newInstance(size,
		maxValue, label);
	memory.add(newMemory);
	return newMemory;
    }

    public MemoryArrayImpl allocateShuffled(int size, String label) {
	final MemoryArrayImpl newMemory = MemoryArrayImpl.shuffledInstance(
		size, label);
	memory.add(newMemory);
	return newMemory;
    }

    public double getTime() {
	int r = 0;

	for (MemoryArrayImpl a : memory) {
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
