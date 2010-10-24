package no.karevoll.sorting.memory;

public interface MemoryManager {
    public MemoryArray allocate(int size, String label);

    public void free(MemoryArray memoryArray);
}
