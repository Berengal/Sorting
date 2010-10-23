package no.karevoll.sorting.algorithms;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import no.karevoll.sorting.App;
import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.IMemoryManager;
import no.karevoll.sorting.memory.MemoryArray;

public class ParallellMergeSort implements SortingAlgorithm {

    Executor executor;
    MemoryArray input, scratchMemory;
    Object done;

    @Override
    public void sort(MemoryArray input, IMemoryManager memoryManager) {
	this.input = input;
	scratchMemory = memoryManager.allocate(input.getSize(),
		"Scratch memory");
	executor = new ThreadPoolExecutor(App.THREAD_POOL_SIZE,
		App.THREAD_POOL_SIZE, 0, TimeUnit.SECONDS,
		new LinkedBlockingQueue<Runnable>());
	done = new Object();
	executor.execute(new Merger(0, input.getSize(), new Counter(1),
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

	int start, end, middle;
	Counter counter;
	Runnable parentMerge;

	public Merger(int start, int end, Counter counter, Runnable parentMerge) {
	    this.start = start;
	    this.end = end;
	    middle = (start + end) / 2;
	    this.counter = counter;
	    this.parentMerge = parentMerge;
	}

	@Override
	public void run() {
	    if (start >= end - 1) {
		if (counter.dec()) {
		    parentMerge.run();
		}
		return;
	    }
	    Counter childCounter = new Counter(2);
	    Runnable magic = new Runnable() {

		@Override
		public void run() {
		    merge();
		    if (counter.dec()) {
			parentMerge.run();
		    }
		}

	    };

	    Merger leftChild = new Merger(start, middle, childCounter, magic);
	    Merger rightChild = new Merger(middle, end, childCounter, magic);
	    executor.execute(leftChild);
	    executor.execute(rightChild);
	}

	private void merge() {

	    int i = start, j = middle;

	    while (i < middle && j < end) {
		if (input.read(i).compareTo((input.read(j))) < 0) {
		    scratchMemory.insert(input.remove(i), i + j - middle);
		    i++;
		} else {
		    scratchMemory.insert(input.remove(j), i + j - middle);
		    j++;
		}
	    }

	    while (i < middle) {
		scratchMemory.insert(input.remove(i), i + j - middle);
		i++;
	    }
	    while (j < end) {
		scratchMemory.insert(input.remove(j), i + j - middle);
		j++;
	    }

	    for (int index = start; index < end; index++) {
		input.insert(scratchMemory.remove(index), index);
	    }
	}

    }

    class Counter {

	private int count;

	public Counter(int count) {
	    this.count = count;
	}

	synchronized boolean dec() {
	    count--;
	    return count == 0;
	}

    }
}
