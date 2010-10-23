package no.karevoll.sorting;

import no.karevoll.sorting.memory.IMemoryManager;
import no.karevoll.sorting.memory.MemoryArray;

public interface SortingAlgorithm {

    void sort(MemoryArray input, IMemoryManager memoryManager);

}
