package no.karevoll.sorting.memory;

import java.util.ArrayList;
import java.util.List;

import no.karevoll.sorting.App;

public class MemoryManagerImpl implements MemoryManager {

  private int reads = 0;
  private int removes = 0;
  private int writes = 0;
  private int swaps = 0;
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

  public MemoryArrayImpl allocateRandom(int size, int maxValue, String label) {
    final MemoryArrayImpl newMemory = MemoryArrayImpl.randomInstance(size, maxValue, label);
    memory.add(newMemory);
    return newMemory;
  }

  public double getTime() {
    int r = 0;

    r += reads * App.READ_TIME;
    r += writes * App.WRITE_TIME;
    r += removes * App.REMOVE_TIME;
    r += swaps * App.SWAP_TIME;

    for (MemoryArrayImpl a : memory) {
      r += a.getReads() * App.READ_TIME;
      r += a.getWrites() * App.WRITE_TIME;
      r += a.getRemoves() * App.REMOVE_TIME;
      r += a.getSwaps() * App.SWAP_TIME;
    }

    return r / 1000.0;
  }

  public void free(MemoryArray memoryArray) {
    if (!memory.contains(memoryArray)) {
      throw new IllegalArgumentException();
    }
    MemoryArrayImpl array = (MemoryArrayImpl) memoryArray;

    reads += array.getReads();
    writes += array.getWrites();
    removes += array.getRemoves();
    swaps += array.getSwaps();

    memory.remove(array);
  }

}
