package no.karevoll.sorting;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.Timer;

import no.karevoll.sorting.algorithms.BubbleSort;
import no.karevoll.sorting.algorithms.HeapSort;
import no.karevoll.sorting.algorithms.InsertionSort;
import no.karevoll.sorting.algorithms.MergeSort;
import no.karevoll.sorting.algorithms.QuickSort;
import no.karevoll.sorting.algorithms.SelectionSort;
import no.karevoll.sorting.algorithms.ShakerSort;
import no.karevoll.sorting.algorithms.ShellSort;
import no.karevoll.sorting.algorithms.StoogeSort;
import no.karevoll.sorting.memory.MemoryArrayImpl;
import no.karevoll.sorting.memory.MemoryManagerImpl;
import no.karevoll.sorting.memory.SortDisplay;

public class App {
    static SortingAlgorithm[] algos = { new StoogeSort(), new BubbleSort(),
	    new ShakerSort(), new SelectionSort(), new InsertionSort(),
	    new ShellSort(), new HeapSort(), new MergeSort(), new QuickSort(),
    /* new ParallellMergeSort(), new ParallellQuickSort() */};

    // public static final int READ_TIME = 25;
    // public static final int WRITE_TIME = 50;
    // public static final int COMPARE_TIME = 100;
    // public static final long REMOVE_TIME = 0;
    // public static final long SWAP_TIME = 200;
    // public static final int THREAD_POOL_SIZE = 4;
    //
    // public static final int LIST_SIZE = 10;
    // public static final int MAX_VALUE = 100;

    public static final int READ_TIME = 1;
    public static final int WRITE_TIME = 2;
    public static final int COMPARE_TIME = 0;
    public static final int REMOVE_TIME = 0;
    public static final int SWAP_TIME = 6;
    public static final int THREAD_POOL_SIZE = 256;

    public static final int LIST_SIZE = 100;
    public static final int MAX_VALUE = 100;

    public static void main(String[] args) throws InterruptedException {
	// long t0 = System.currentTimeMillis();
	//
	// double simulatedTime = test(algos[0], true);
	//
	// double realTime = (System.currentTimeMillis() - t0) / 1000.0;
	// System.out.printf("Real: %.3fs%n", realTime);
	// System.out.printf("Simulated: %.3fs%n", simulatedTime);
	// System.out.printf("Speedup: %.1f%%%n", simulatedTime / realTime *
	// 100);

	System.out.println(algos[getMin()].getClass().getSimpleName());

	System.exit(0);
    }

    private static int getMin() {
	final double[] times = new double[algos.length];
	Thread[] threads = new Thread[algos.length];
	for (int i = 0; i < algos.length; i++) {
	    final int index = i;
	    threads[i] = new Thread() {
		@Override
		public void run() {
		    times[index] = test(algos[index], true);
		}
	    };
	    threads[i].start();
	}

	double minValue = Double.MAX_VALUE;
	int min = 0;
	for (int i = 0; i < algos.length; i++) {
	    try {
		threads[i].join();
	    } catch (InterruptedException e) {
		continue;
	    }
	    if (times[i] < minValue) {
		minValue = times[i];
		min = i;
	    }
	}
	return min;
    }

    private static double test(SortingAlgorithm algo, boolean display) {
	MemoryManagerImpl manager = MemoryManagerImpl.newInstance(MAX_VALUE);
	MemoryArrayImpl memory = manager.allocateShuffled(LIST_SIZE, algo
		.getClass().getSimpleName());

	JFrame frame = null;

	if (display)
	    frame = newFrame(memory);

	algo.sort(memory, manager);

	if (display)
	    frame.dispose();

	return manager.getTime();
    }

    private static JFrame newFrame(MemoryArrayImpl memory) {
	final JFrame mainFrame = new JFrame(memory.getLabel());
	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	final Container contents = mainFrame.getContentPane();
	mainFrame.setLayout(new BoxLayout(contents, BoxLayout.X_AXIS));
	final SortDisplay sortDisplay = SortDisplay.getInstance(memory);
	contents.add(sortDisplay);
	Timer timer = new Timer(50, new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		sortDisplay.repaint();
	    }
	});
	timer.start();
	mainFrame.pack();
	mainFrame.setExtendedState(mainFrame.getExtendedState()
		| JFrame.MAXIMIZED_BOTH);
	mainFrame.setVisible(true);

	return mainFrame;
    }
}
