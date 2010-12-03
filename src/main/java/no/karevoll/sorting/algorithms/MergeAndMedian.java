package no.karevoll.sorting.algorithms;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.Element;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;
import no.karevoll.sorting.memory.MemorySlice;

public class MergeAndMedian implements SortingAlgorithm {

  @Override
  public void sort(MemoryArray input, MemoryManager memoryManager) {
    MemorySlice memory = new MemorySlice(input);
    sort(memory, memoryManager, true);
  }

  private void sort(MemorySlice memory, MemoryManager manager, boolean last) {
    if (memory.getSize() <= 1) {
      return;
    }
    if (memory.getSize() == 2) {
      memory.compareAndSwap(0, 1);
      return;
    }
    if (memory.getSize() <= 16) {
      int spot = partision(memory, 0);
      quicksort(memory.sliceLeft(spot));
      quicksort(memory.sliceRight(spot + 1));
      return;
    }

    int p = memory.getSize() / 2;
    MemorySlice left = memory.sliceLeft(p);
    MemorySlice right = memory.sliceRight(p);
    sort(left, manager, false);
    int spot = partision(right, left.read(p / 2));
    MemorySlice rl = right.sliceLeft(spot);
    MemorySlice rr = right.sliceRight(spot);
    int spot2 = 100, spot3 = 100;
    //try{
    spot2 = partision(rl, left.read(p / 4));
    spot3 = partision(rr, left.read(p * 3 / 4));
    quicksort(rl.sliceLeft(spot2));
    quicksort(rl.sliceRight(spot2));
    quicksort(rr.sliceLeft(spot3));
    quicksort(rr.sliceRight(spot3));
    /*} catch (java.lang.ArrayIndexOutOfBoundsException e){
    System.out.println(left.getSize());
    System.out.println(right.getSize());
    System.out.println(p);
    System.out.println(rl.getSize());
    System.out.println(spot2);
    System.out.println(spot3);
    System.exit(1);
    }*/
    //quicksort(right);

    MemoryArray scratch = manager.allocate(memory.getSize(), "Scratch");

    int i = 0, j = 0;

    boolean done = false;
    Element leftElement = left.remove(0);
    Element rightElement = right.remove(0);
    while (!done) {
      if (leftElement.compareTo(rightElement) < 0) {
        scratch.insert(leftElement, i++ + j);
        if (i < left.getSize()) {
          leftElement = left.remove(i);
        } else {
          scratch.insert(rightElement, i + j++);
          done = true;
        }
      } else {
        scratch.insert(rightElement, i + j++);
        if (j < right.getSize()) {
          rightElement = right.remove(j);
        } else {
          scratch.insert(leftElement, i++ + j);
          done = true;
        }
      }
    }

    while (i < left.getSize()) {
      scratch.insert(left.remove(i), i + j);
      i++;
    }

    while (j < right.getSize()) {
      scratch.insert(right.remove(j), i + j);
      j++;
    }

    for (i = 0; i < scratch.getSize(); i++) {
      Element e = scratch.read(i);
      scratch.markRemoved(i);
      if (last) {
        e.markSorted();
      }
      memory.insert(e, i);
    }

    manager.free(scratch);
  }

  private void quicksort(MemorySlice memory) {
    switch (memory.getSize()) {
      case 0:
        return;
      case 1:
        memory.markSorted(0);
        return;
      case 2:
        Element a = memory.read(0);
        Element b = memory.read(1);

        if (a.compareTo(b) > 0) {
          memory.set(a, 1);
          memory.set(b, 0);
        }
        a.markSorted();
        b.markSorted();
        return;
      default:
    }
    int spot = partision(memory, 0);

    quicksort(memory.sliceLeft(spot));
    quicksort(memory.sliceRight(spot + 1));
  }

  public static int partision(MemorySlice memory, int pivotIndex) {
    Element pivot = memory.remove(pivotIndex);
    if (pivotIndex != 0) {
      memory.insert(memory.remove(0), pivotIndex);
    }

    int spot = 0;
    int direction = -1;

    int min = 0;
    int max = memory.getSize() - 1;
    int index = max;
    while (index != spot) {
      Element c = memory.read(index);
      if (c.compareTo(pivot) == direction) {
        memory.markRemoved(index);
        memory.insert(c, spot);
        spot = index;
        if (direction == -1) {
          max = index - 1;
          index = min;
        } else {
          min = index + 1;
          index = max;
        }
        direction *= -1;
      } else {
        index += direction;
      }
    }

    memory.insert(pivot, spot);
    pivot.markSorted();

    return spot;
  }

  public int partision(MemorySlice memory, Element pivot) {
    int spot = 0;
    int direction = -1;
    Element extra = memory.remove(0);
    int min = 0;
    int max = memory.getSize() - 1;
    int index = max;
    while (spot != index) {
      Element c = memory.read(index);
      if (c.compareTo(pivot) == direction) {
        memory.markRemoved(index);
        memory.insert(c, spot);
        spot = index;
        if (direction == -1) {
          max = index - 1;
          index = min;
        } else {
          min = index + 1;
          index = max;
        }
        direction *= -1;
      } else {
        index += direction;
      }
    }
    memory.insert(extra, spot);
    if (extra.compareTo(pivot) < 0) {
      spot++;
    }
    return spot;
  }

}
