package no.karevoll.sorting;



import no.karevoll.sorting.memory.SortDisplay;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.Timer;
import no.karevoll.sorting.algorithms.HeapSort;
import no.karevoll.sorting.algorithms.MergeSort;
import no.karevoll.sorting.algorithms.ParallellQuickSort;
import no.karevoll.sorting.algorithms.QuickSort;
import no.karevoll.sorting.memory.MemoryArray;
import no.karevoll.sorting.memory.MemoryManager;

/**
 * Hello world!
 *
 */
public class App {
//	public static final int READ_TIME = 25;
//	public static final int WRITE_TIME = 50;
//	public static final int COMPARE_TIME = 100;
//	public static final long REMOVE_TIME = 0;
//	public static final long SWAP_TIME = 200;
//	public static final int THREAD_POOL_SIZE = 4;
//
//	public static final int LIST_SIZE = 50;
//	public static final int MAX_VALUE = 100;
	
	public static final int READ_TIME = 0;
	public static final int WRITE_TIME = 0;
	public static final int COMPARE_TIME = 0;
	public static final int REMOVE_TIME = 1;
	public static final int SWAP_TIME = 2;
	public static final int THREAD_POOL_SIZE = 16;
	
	public static final int LIST_SIZE = 10000;
	public static final int MAX_VALUE = 100;
	
	public static void main(String[] args) throws InterruptedException {
		final JFrame mainFrame = new JFrame("Sort displayer");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final Container contents = mainFrame.getContentPane();
		mainFrame.setLayout(new BoxLayout(contents, BoxLayout.X_AXIS));

		MemoryArray memory = MemoryArray.shuffledInstance(LIST_SIZE, null);
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
    mainFrame.setExtendedState(mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		mainFrame.setVisible(true);
    
		
		MemoryManager manager = MemoryManager.newInstance(LIST_SIZE);
		
//		//O(n^3)
//		new StoogeSort().sort(memory, manager);
//		memory.shuffle();
//		new ParallellQuickSort().sort(memory, manager);
//		memory.shuffle();
//		//O(n^2)
//		new BubbleSort().sort(memory, manager);
//		memory.shuffle();
//		new ParallellQuickSort().sort(memory, manager);
//		memory.shuffle();
//		new ShakerSort().sort(memory, manager);
//		memory.shuffle();
//		new ParallellQuickSort().sort(memory, manager);
//		memory.shuffle();
//		new SelectionSort().sort(memory, manager);
//		memory.shuffle();
//		new ParallellQuickSort().sort(memory, manager);
//		memory.shuffle();
//		new InsertionSort().sort(memory, manager);
//		memory.shuffle();
//		new ParallellQuickSort().sort(memory, manager);
//		memory.shuffle();
//		new ShellSort().sort(memory, manager);
//		memory.shuffle();
//		new ParallellQuickSort().sort(memory, manager);
//		memory.shuffle();
		//O(n log n)
		new ParallellQuickSort().sort(memory, manager);
		new HeapSort().sort(memory, manager);
		memory.shuffle();
		new ParallellQuickSort().sort(memory, manager);
		memory.shuffle();
		new MergeSort().sort(memory, manager);
		memory.shuffle();
		new ParallellQuickSort().sort(memory, manager);
		memory.shuffle();
		new QuickSort().sort(memory, manager);
		memory.shuffle();
		new ParallellQuickSort().sort(memory, manager);
	}

}


