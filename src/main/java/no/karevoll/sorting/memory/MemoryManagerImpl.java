package no.karevoll.sorting.memory;

import java.util.ArrayList;
import java.util.List;

import no.karevoll.sorting.Counter;
import no.karevoll.sorting.Settings;

public class MemoryManagerImpl implements MemoryManager {

    public static MemoryManagerImpl newInstance(Settings settings) {
	return new MemoryManagerImpl(settings);
    }

    private Counter counter;
    private final List<MemoryArrayImpl> memory = new ArrayList<MemoryArrayImpl>();
    private final Settings settings;

    private MemoryManagerImpl(Settings settings) {
	this.settings = settings;
	this.counter = new Counter(settings);
    }

    public MemoryArray allocate(int size, String label) {
	final MemoryArrayImpl newMemory = MemoryArrayImpl.newInstance(size,
		settings, counter, label);
	memory.add(newMemory);
	return newMemory;
    }

    public MemoryArrayImpl allocateRandom(int size, int maxValue, String label) {
	final MemoryArrayImpl newMemory = MemoryArrayImpl.randomInstance(
		settings, counter, label);
	memory.add(newMemory);
	return newMemory;
    }

    public MemoryArrayImpl allocateShuffled(String label) {
	final MemoryArrayImpl newMemory = MemoryArrayImpl.shuffledInstance(
		settings, counter, label);
	memory.add(newMemory);
	return newMemory;
    }

    public void free(MemoryArray memoryArray) {
	if (!memory.contains(memoryArray)) {
	    throw new IllegalArgumentException();
	}
	MemoryArrayImpl array = (MemoryArrayImpl) memoryArray;

	memory.remove(array);
    }

    public double getTime() {
	return counter.getTime();
    }

}
