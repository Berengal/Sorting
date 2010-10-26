package no.karevoll.sorting;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import no.karevoll.sorting.algorithms.BubbleSort;
import no.karevoll.sorting.algorithms.DualSelectionSort;
import no.karevoll.sorting.algorithms.HeapSort;
import no.karevoll.sorting.algorithms.InsertionSort;
import no.karevoll.sorting.algorithms.ParallellMergeSort;
import no.karevoll.sorting.algorithms.ParallellQuickSort;

import no.karevoll.sorting.algorithms.MERGETRON2000;
import no.karevoll.sorting.algorithms.MergeSort;
import no.karevoll.sorting.algorithms.QuickSort;
import no.karevoll.sorting.algorithms.SelectionSort;
import no.karevoll.sorting.algorithms.ShakerSort;
import no.karevoll.sorting.algorithms.ShellSort;
import no.karevoll.sorting.memory.MemoryArrayImpl;
import no.karevoll.sorting.memory.MemoryManagerImpl;
import no.karevoll.sorting.memory.SortDisplay;

public class App {

  public static final int DELAY_MULTIPLIER = 2;

  // public static final int READ_TIME = 25;
  // public static final int WRITE_TIME = 50;
  // public static final int COMPARE_TIME = 100;
  // public static final long REMOVE_TIME = 0;
  // public static final long SWAP_TIME = 200;
  // public static final int THREAD_POOL_SIZE = 4;
  //
  // public static final int LIST_SIZE = 10;
  // public static final int MAX_VALUE = 100;

  public static final int READ_TIME = 1 * DELAY_MULTIPLIER;
  public static final int WRITE_TIME = 2 * DELAY_MULTIPLIER;
  public static final int COMPARE_TIME = 2 * DELAY_MULTIPLIER;
  public static final int REMOVE_TIME = 0;
  public static final int SWAP_TIME = 6 * DELAY_MULTIPLIER;
  
  public static final int LIST_SIZE = 100;
  public static final int MAX_VALUE = 100;

  public static final int THREAD_POOL_SIZE = 4;
  public static final int PARALLELL_CUTOFF = 8;
  
  public static final int FRAME_WIDTH = 1024;
  public static final int FRAME_HEIGHT = 768;
  public static final boolean FULLSCREEN = true;
  static SortingAlgorithm[] algos = { //    new StoogeSort()
  // new BubbleSort()
  //, new ShakerSort()
  //, new SelectionSort()
  //, new InsertionSort()
  //, new ShellSort()
  //, new HeapSort()
  //, new MergeSort()
  //, new QuickSort()
  // new ParallellMergeSort()
  // new ParallellQuickSort()
  //          new SuperParallellMergeSort(PARALLELL_CUTOFF)
  };
  public static void main(String[] args) throws InterruptedException {


    test(new BubbleSort(), true);
    test(new ShakerSort(), true);
    test(new SelectionSort(), true);
    test(new DualSelectionSort(), true);
    test(new InsertionSort(), true);
    test(new ShellSort(), true);
    test(new HeapSort(), true);
    test(new MergeSort(), true);
    test(new QuickSort(), true);

    test(new ParallellMergeSort(), true);
    test(new ParallellQuickSort(), true);
    test(new MERGETRON2000(PARALLELL_CUTOFF), true);

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
    MemoryArrayImpl memory = manager.allocateShuffled(LIST_SIZE, algo.getClass().getSimpleName());

    JFrame frame = null;

    if (display) {
      frame = newFrame(memory);
      JOptionPane.showMessageDialog(frame, optionsString() + "\n" + algo.getClass().getSimpleName() + "\nStart");
    }
    long start = System.currentTimeMillis();

    algo.sort(memory, manager);

    long end = System.currentTimeMillis();
    if (display) {
      JOptionPane.showMessageDialog(frame, algo.getClass().getSimpleName() + "\nDone\n\nTime: " + String.format("%.2fs", (end - start) / 1000.0));
      frame.dispose();
    }

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

    if (FULLSCREEN) {
      mainFrame.setExtendedState(mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    } else {
      mainFrame.setMaximumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
    }
    mainFrame.setVisible(true);

    return mainFrame;
  }

  private static String optionsString() {
    StringBuilder sb = new StringBuilder();
    String fs = "%15s: %d%n";
    sb.append(String.format(fs, "Elements", LIST_SIZE)).append(String.format(fs, "Threads", THREAD_POOL_SIZE)).append(String.format(fs, "Delay x", DELAY_MULTIPLIER));
    return sb.toString();
  }

}
