package no.karevoll.sorting;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.Timer;

import no.karevoll.sorting.algorithms.ParallellQuickSort;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;
import no.karevoll.sorting.memory.SortDisplay;

/**
 * Hello world!
 * 
 */
public class App {
    // public static final int READ_TIME = 250;
    // public static final int WRITE_TIME = 500;
    // public static final int COMPARE_TIME = 1000;
    // public static final long REMOVE_TIME = 00;
    // public static final long SWAP_TIME = 2000;
    // public static final int THREAD_POOL_SIZE = 4;
    //
    // public static final int LIST_SIZE = 50;
    // public static final int MAX_VALUE = 100;

    public static final int READ_TIME = 1;
    public static final int WRITE_TIME = 1;
    public static final int COMPARE_TIME = 0;
    public static final int REMOVE_TIME = 0;
    public static final int SWAP_TIME = 2;
    public static final int THREAD_POOL_SIZE = 128;

    public static final int LIST_SIZE = 10000;
    public static final int MAX_VALUE = 10000;

    public static void main(String[] args) throws InterruptedException {
	MemoryManager manager = MemoryManager.newInstance(MAX_VALUE);

	final JFrame mainFrame = new JFrame("Sort displayer");
	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	final Container contents = mainFrame.getContentPane();
	mainFrame.setLayout(new BoxLayout(contents, BoxLayout.X_AXIS));

	MemoryArray memory = manager
		.allocateShuffled(LIST_SIZE, "Main memmory");
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

	long t0 = System.currentTimeMillis();

	// //O(n^3)
	// new StoogeSort().sort(memory, manager);
	// memory.shuffle();
	// //O(n^2)
	// new BubbleSort().sort(memory, manager);
	// memory.shuffle();
	// new ShakerSort().sort(memory, manager);
	// memory.shuffle();
	// new SelectionSort().sort(memory, manager);
	// memory.shuffle();
	// new InsertionSort().sort(memory, manager);
	// memory.shuffle();
	// new ShellSort().sort(memory, manager);
	// memory.shuffle();
	// O(n log n)
	// new HeapSort().sort(memory, manager);
	// memory.shuffle();
	// new MergeSort().sort(memory, manager);
	// memory.shuffle();
	// new QuickSort().sort(memory, manager);
	// memory.shuffle();
	new ParallellQuickSort().sort(memory, manager);
	// memory.shuffle();
	// new ParallellMergeSort().sort(memory, manager);

	double realTime = (System.currentTimeMillis() - t0) / 1000.0;
	double simulatedTime = manager.getTime();
	System.out.printf("Real: %.3fs%n", realTime);
	System.out.printf("Simulated: %.3fs%n", simulatedTime);
	System.out.printf("Speedup: %.1f%%%n", simulatedTime / realTime * 100);

	System.exit(0);
    }
}
