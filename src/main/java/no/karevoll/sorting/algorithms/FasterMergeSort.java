/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.Element;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;
import no.karevoll.sorting.memory.MemorySlice;

/**
 *
 * @author berengal
 */
public class FasterMergeSort implements SortingAlgorithm {

  @Override
  public void sort(MemoryArray input, MemoryManager memoryManager) {
    MemorySlice memory = new MemorySlice(input);
    MemoryArray scratchMemory = memoryManager.allocate(input.getSize(), "Scratch memory");
    MemorySlice scratch = new MemorySlice(scratchMemory);
    boolean listInScratch = sort(memory, scratch);
    if (listInScratch) {
      move(scratch, memory);
    }
    memoryManager.free(scratchMemory);
  }

  private boolean sort(MemorySlice input, MemorySlice scratch) {
    if (input.getSize() <= 1) {
      return false;
    } else {
      int middle = input.getSize() / 2;
      MemorySlice left, right, leftScratch, rightScratch;
      left = input.sliceLeft(middle);
      right = input.sliceRight(middle);
      leftScratch = scratch.sliceLeft(middle);
      rightScratch = scratch.sliceRight(middle);
      boolean leftInScratch = sort(left, leftScratch);
      boolean rightInScratch = sort(right, rightScratch);
      if (leftInScratch && rightInScratch) {
        merge(leftScratch, rightScratch, input);
        return false;
      } else if (leftInScratch) {
        move(leftScratch, left);
        merge(left, right, scratch);
        return true;
      } else if (rightInScratch) {
        merge(left, rightScratch, scratch);
        return true;
      } else {
        merge(left, right, scratch);
        return true;
      }
    }
  }

  private void merge(MemorySlice left, MemorySlice right, MemorySlice to) {
    int i = 0, j = 0;

    while (i < left.getSize() && j < right.getSize()) {
      Element leftElem = left.read(i), rightElem = right.read(j);
      if (leftElem.compareTo(rightElem) < 0) {
        left.markRemoved(i);
        to.insert(leftElem, i+j);
        i++;
      } else {
        right.markRemoved(j);
        to.insert(rightElem, i + j);
        j++;
      }
    }
    while (i < left.getSize()) {
      to.insert(left.remove(i), i + j);
      i++;
    }
    while (j < right.getSize()) {
      to.insert(right.remove(j), i + j);
      j++;
    }
  }

  private void move(MemorySlice from, MemorySlice to) {
    for(int i = 0; i < from.getSize(); i++) {
      to.insert(from.remove(i), i);
    }
  }
}
