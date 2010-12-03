package no.karevoll.sorting.memory;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.Timer;

import no.karevoll.sorting.Counter;
import no.karevoll.sorting.Settings;

public class MemoryManagerImpl implements MemoryManager {

  public static MemoryManagerImpl newInstance(JFrame frame, Settings settings) {
    return new MemoryManagerImpl(frame, settings);
  }

  private Counter counter;
  private final List<MemoryArrayImpl> memory = new ArrayList<MemoryArrayImpl>();
  private final Settings settings;
  private JFrame frame;
  private MemoryManagerImpl(JFrame frame, Settings settings) {
    this.settings = settings;
    this.frame = frame;
    this.counter = new Counter(settings);
  }

  private void add(MemoryArrayImpl newMemory) {
    if (settings.DISPLAY) {
      final SortDisplay sortDisplay = SortDisplay.getInstance(newMemory,
              settings);
      frame.getContentPane().add(sortDisplay);
      final Timer timer = new Timer(50, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            sortDisplay.repaint();
        }
      });
      timer.start();
      if (settings.FULLSCREEN) {
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
      } else {
        frame.setMaximumSize(new Dimension(settings.FRAME_WIDTH, settings.FRAME_HEIGHT));
      }
      frame.pack();
    }
    memory.add(newMemory);
  }

  @Override
  public MemoryArray allocate(int size, String label) {
    final MemoryArrayImpl newMemory = MemoryArrayImpl.newInstance(size,
            settings, counter, label);
    add(newMemory);
    return newMemory;
  }

  public MemoryArrayImpl allocateRandom(int size, int maxValue, String label) {
    final MemoryArrayImpl newMemory = MemoryArrayImpl.randomInstance(settings, counter, label);
    add(newMemory);
    return newMemory;
  }

  public MemoryArrayImpl allocateShuffled(String label) {
    final MemoryArrayImpl newMemory = MemoryArrayImpl.shuffledInstance(settings, counter, label);
    add(newMemory);
    return newMemory;
  }

  public void free(MemoryArray memoryArray) {
    if (!memory.contains(memoryArray)) {
      throw new IllegalArgumentException();
    }
    MemoryArrayImpl array = (MemoryArrayImpl) memoryArray;

    memory.remove(array);
  }

  public double getTime() {
    return counter.getTime();
  }

}
