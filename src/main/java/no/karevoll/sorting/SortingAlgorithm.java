package no.karevoll.sorting;

import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;

public interface SortingAlgorithm {

    void sort(MemoryArray input, MemoryManager memoryManager);

}
