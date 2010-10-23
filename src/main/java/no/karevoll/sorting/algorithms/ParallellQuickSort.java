/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.karevoll.sorting.algorithms;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import no.karevoll.sorting.App;
import no.karevoll.sorting.SortingAlgorithm;
import no.karevoll.sorting.memory.Element;
import no.karevoll.sorting.memory.IMemoryManager;
import no.karevoll.sorting.memory.MemoryArray;

/**
 * 
 * @author berengal
 */
public class ParallellQuickSort implements SortingAlgorithm {

    private ExecutorService executor;

    @Override
    public void sort(MemoryArray input, IMemoryManager memoryManager) {
	BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
	Counter c = new Counter(input.getSize());
	executor = new ThreadPoolExecutor(App.THREAD_POOL_SIZE,
		App.THREAD_POOL_SIZE, 1, TimeUnit.SECONDS, queue);
	sort(input, 0, input.getSize(), c);
	c.waitZero();
    }

    private void sort(final MemoryArray input, final int start, final int end,
	    final Counter c) {
	switch (end - start) {
	case 0:
	    return;
	case 1:
	    input.read(start).markSorted();
	    c.dec();
	    return;
	case 2:
	    if (input.read(start).compareTo(input.read(start + 1)) > 0) {
		input.swap(start, start + 1);
	    }
	    input.read(start).markSorted();
	    c.dec();
	    input.read(start + 1).markSorted();
	    c.dec();
	    return;
	default:
	}

	Element pivot = input.remove(start);

	int spot = start;
	int direction = -1;

	int min = start + 1;
	int max = end - 1;
	int index = max;
	while (index != spot) {
	    if (input.read(index).compareTo(pivot) == direction) {
		input.insert(input.remove(index), spot);
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
	input.insert(pivot, spot);
	pivot.markSorted();
	c.dec();
	final int firstSpot = spot;
	if (end - start > 0) {
	    executor.execute(new Runnable() {
		public void run() {
		    sort(input, start, firstSpot, c);
		}
	    });
	    executor.execute(new Runnable() {
		public void run() {
		    sort(input, firstSpot + 1, end, c);
		}
	    });
	} else {
	    sort(input, start, firstSpot, c);
	    sort(input, firstSpot + 1, end, c);
	}
    }

    class Counter {
	private int n;

	public Counter(int n) {
	    this.n = n;
	}

	synchronized void waitZero() {
	    while (n > 0) {
		try {
		    wait();
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}

	synchronized void dec() {
	    n--;
	    notifyAll();
	}
    }

}