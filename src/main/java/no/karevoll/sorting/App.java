package no.karevoll.sorting;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import no.karevoll.sorting.algorithms.MERGETRON;

import no.karevoll.sorting.memory.MemoryArrayImpl;
import no.karevoll.sorting.memory.MemoryManagerImpl;

public class App {

  public static void main(String[] args) throws InterruptedException {
    Settings settings = new Settings();

//     test(new StoogeSort(), settings);
//     test(new BubbleSort(), settings);
//     test(new ShakerSort(), settings);
//     test(new SelectionSort(), settings);
//     test(new DualSelectionSort(), settings);
//     test(new InsertionSort(), settings);
//     test(new BinaryInsertionSort(), settings);
//     test(new ShellSort(), settings);
//     test(new HeapSort(), settings);
//     test(new MergeSort(), settings);
//     test(new FasterMergeSort(), settings);
//     test(new QuickSort(), settings);
//     test(new Median3QuickSort(), settings);
//
//     test(new ParallellMergeSort(settings.THREAD_POOL_SIZE), settings);
//     test(new ParallellQuickSort(settings.THREAD_POOL_SIZE), settings);
     test(new MERGETRON(settings.THREAD_POOL_SIZE, settings.PARALLELL_CUTOFF), settings);

    // drawGraf(new BubbleSort(), 10000, 1000);
    // drawGraf(new ShakerSort(), 10000, 1000);
    // drawGraf(new SelectionSort(), 10000, 1000);
    // drawGraf(new DualSelectionSort(), 10000, 1000);
    // drawGraf(new InsertionSort(), 10000, 1000);
    // drawGraf(new BinaryInsertionSort(), 10000, 1000);
    // drawGraf(new QuickSort(), 10000, 1000);
    // drawGraf(new Median3QuickSort(), 10000, 1000);
    // drawGraf(new MergeSort(), 10000, 1000);
    // drawGraf(new ParallellQuickSort(4), 10000, 1000);
    // drawGraf(new ParallellMergeSort(4), 10000, 1000);

     System.exit(0);
  }

  private static void drawGraph(SortingAlgorithm algo, int max, int interval) {
    System.out.printf("%20s: ", algo.getClass().getSimpleName());

    for (int i = interval; i <= max; i += interval) {
      Settings settings = new Settings();
      settings.DISPLAY = false;
      settings.LIST_SIZE = i;
      System.out.printf("%6.3fs ", test(algo, settings) / i);
    }

    System.out.println();
  }

  private static double test(SortingAlgorithm algo, Settings settings) {

    JFrame frame = null;

    if (settings.DISPLAY) {
      frame = newFrame(settings);
    }

    MemoryManagerImpl manager = MemoryManagerImpl.newInstance(frame, settings);
    MemoryArrayImpl memory = manager.allocateShuffled(algo.getClass().getSimpleName());

    JOptionPane.showMessageDialog(frame, optionsString(settings) + "\n"
              + algo.getClass().getSimpleName() + "\nStart");
    long start = System.currentTimeMillis();

    algo.sort(memory, manager);

    long end = System.currentTimeMillis();
    if (settings.DISPLAY) {
      JOptionPane.showMessageDialog(frame, algo.getClass().getSimpleName()
              + "\nDone\n\nReal time: "
              + String.format("%.2fs", (end - start) / 1000.0)
              + "\nSimulated time: "
              + String.format("%.2fs", manager.getTime()));
      frame.dispose();
    } else {
      // System.out.println(algo.getClass().getSimpleName()
      // + "\nReal time: "
      // + String.format("%.2fs", (end - start) / 1000.0)
      // + "\nSimulated time: "
      // + String.format("%.2fs", manager.getTime()) + "\n\n");
    }

    return manager.getTime();
  }

  private static JFrame newFrame(Settings settings) {
    final JFrame mainFrame = new JFrame("Sorting");
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    final Container contents = mainFrame.getContentPane();
    mainFrame.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
    Timer timer = new Timer(50, new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        mainFrame.repaint();
      }

    });
    timer.start();
    mainFrame.pack();

    if (settings.FULLSCREEN) {
      mainFrame.setExtendedState(mainFrame.getExtendedState()
              | JFrame.MAXIMIZED_BOTH);
    } else {
      mainFrame.setMaximumSize(new Dimension(settings.FRAME_WIDTH,
              settings.FRAME_HEIGHT));
    }
    mainFrame.setVisible(true);

    return mainFrame;
  }

  private static String optionsString(Settings settings) {
    StringBuilder sb = new StringBuilder();
    String fs = "%15s: %d%n";
    sb.append(String.format(fs, "Elements", settings.LIST_SIZE)).append(String.format(fs, "Threads", settings.THREAD_POOL_SIZE)).append(String.format(fs, "Delay x", settings.DELAY_MULTIPLIER));
    return sb.toString();
  }

}
