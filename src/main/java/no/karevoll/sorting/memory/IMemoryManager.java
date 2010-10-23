package no.karevoll.sorting.memory;

public interface IMemoryManager {
    public MemoryArray allocate(int size, String label);

    public void free(MemoryArray memoryArray);
}
