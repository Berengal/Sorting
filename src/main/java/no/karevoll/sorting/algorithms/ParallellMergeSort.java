package no.karevoll.sorting.algorithms;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.Element;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;
import no.karevoll.sorting.memory.MemorySlice;
import no.karevoll.sorting.parallel.Counter;

public class ParallellMergeSort implements SortingAlgorithm {
    Executor executor;
    Object done;
    private int threads;

    public ParallellMergeSort(int threads) {
	this.threads = threads;
    }

    @Override
    public void sort(MemoryArray input, MemoryManager memoryManager) {
	MemorySlice memory = new MemorySlice(input);
	MemoryArray scratchMemory = memoryManager.allocate(memory.getSize(),
		"Scratch memory");
	MemorySlice scratch = new MemorySlice(scratchMemory);

	executor = new ThreadPoolExecutor(threads, threads, 0,
		TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

	done = new Object();
	executor.execute(new Merger(memory, scratch, new Counter(1),
		new Runnable() {

		    @Override
		    public void run() {
			synchronized (done) {
			    done.notifyAll();
			}
		    }

		}));
	synchronized (done) {
	    try {
		done.wait();
	    } catch (InterruptedException ex) {
		ex.printStackTrace();
	    }
	}
	memoryManager.free(scratchMemory);
    }

    class Merger implements Runnable {

	MemorySlice left, right, output;
	MemorySlice scratch;
	Counter counter;
	Runnable parentMerge;

	public Merger(MemorySlice input, MemorySlice scratch, Counter counter,
		Runnable parentMerge) {
	    int p = input.getSize() / 2;
	    this.left = input.sliceLeft(p);
	    this.right = input.sliceRight(p);
	    this.output = input;
	    this.scratch = scratch;
	    this.counter = counter;
	    this.parentMerge = parentMerge;
	}

	@Override
	public void run() {
	    if (scratch.getSize() <= 1) {
		if (counter.dec()) {
		    parentMerge.run();
		}
		return;
	    }
	    int middle = scratch.getSize() / 2;
	    Counter childCounter = new Counter(2);
	    Runnable childrenDone = new Runnable() {

		@Override
		public void run() {
		    merge();
		    if (counter.dec()) {
			executor.execute(parentMerge);
		    }
		}

	    };

	    Merger leftChild = new Merger(left, scratch.sliceLeft(middle),
		    childCounter, childrenDone);
	    Merger rightChild = new Merger(right, scratch.sliceRight(middle),
		    childCounter, childrenDone);
	    executor.execute(leftChild);
	    executor.execute(rightChild);
	}

	private void merge() {
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
		Element e = scratch.remove(i);
		output.insert(e, i);
	    }
	}

    }

}
