package no.karevoll.sorting.memory;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.Timer;

import no.karevoll.sorting.Counter;
import no.karevoll.sorting.Settings;

public class MemoryManagerImpl implements MemoryManager {

    public static MemoryManagerImpl newInstance(Settings settings) {
	return new MemoryManagerImpl(settings);
    }

    private Counter counter;
    private final List<MemoryArrayImpl> memory = new ArrayList<MemoryArrayImpl>();
    private final Settings settings;
    private JFrame frame;

    private MemoryManagerImpl(Settings settings) {
	this.settings = settings;
	this.counter = new Counter(settings);
	if (settings.DISPLAY) {
	    frame = new JFrame("Memory");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    final Container contents = frame.getContentPane();
	    frame.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
	    frame.setVisible(true);
	}
    }

    private void add(MemoryArrayImpl newMemory) {
	if (settings.DISPLAY) {
	    final SortDisplay sortDisplay = SortDisplay.getInstance(newMemory,
		    settings);
	    frame.getContentPane().add(sortDisplay);
	    Timer timer = new Timer(50, new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
		    sortDisplay.repaint();
		}

	    });
	    timer.start();
	    if (settings.FULLSCREEN) {
		frame.setExtendedState(frame.getExtendedState()
			| JFrame.MAXIMIZED_BOTH);
	    } else {
		frame.setMaximumSize(new Dimension(settings.FRAME_WIDTH,
			settings.FRAME_HEIGHT));
	    }
	    frame.pack();
	}
	memory.add(newMemory);
    }

    public MemoryArray allocate(int size, String label) {
	final MemoryArrayImpl newMemory = MemoryArrayImpl.newInstance(size,
		settings, counter, label);
	add(newMemory);
	return newMemory;
    }

    public MemoryArrayImpl allocateRandom(int size, int maxValue, String label) {
	final MemoryArrayImpl newMemory = MemoryArrayImpl.randomInstance(
		settings, counter, label);
	if (settings.DISPLAY)
	    frame.setTitle(label);
	add(newMemory);
	return newMemory;
    }

    public MemoryArrayImpl allocateShuffled(String label) {
	final MemoryArrayImpl newMemory = MemoryArrayImpl.shuffledInstance(
		settings, counter, label);
	if (settings.DISPLAY)
	    frame.setTitle(label);
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
