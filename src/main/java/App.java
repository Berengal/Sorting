

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * Hello world!
 *
 */
public class App {
	public static final int READ_TIME = 25;
	public static final int WRITE_TIME = 50;
	public static final int COMPARE_TIME = 100;
	public static final long REMOVE_TIME = 0;
	public static final long SWAP_TIME = 200;
	public static final int THREAD_POOL_SIZE = 4;
	
	public static final int LIST_SIZE = 50;
	public static final int MAX_VALUE = 100;
	
//	public static final int READ_TIME = 0;
//	public static final int WRITE_TIME = 0;
//	public static final int COMPARE_TIME = 0;
//	public static final int REMOVE_TIME = 1;
//	public static final int SWAP_TIME = 2;
//	public static final int THREAD_POOL_SIZE = 16;
//	
//	public static final int LIST_SIZE = 10000;
//	public static final int MAX_VALUE = 100;
	
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

class SortDisplay extends Component {

	public static SortDisplay getInstance(MemoryArray memory) {
		return new SortDisplay(memory);
	}

	private final MemoryArray memory;
	private final Queue<SortingEvent.Event> eventQueue = new ConcurrentLinkedQueue<SortingEvent.Event>();
	{
		setBackground(Color.black);
	}

	private SortDisplay(MemoryArray memory) {
		this.memory = memory;
		setPreferredSize(new Dimension(memory.getSize(),
				memory.getMaxValue()));
	}

	@Override
	public void paint(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		g.setColor(Color.black);
		g.fill(g.getClip());

		Dimension memorySize = new Dimension(memory.getSize(),
				memory.getMaxValue());
		Dimension drawingSize = getSize();
		g.scale(drawingSize.getWidth() / memorySize.getWidth(),
				drawingSize.getHeight() / memorySize.getHeight());
		
		final List<Element> elements = memory.getElements();
		for (int i = 0; i < elements.size(); i++) {
			Element element = elements.get(i);
			if (element != null) {
				g.setColor(element.getState().color);
				g.drawOval(i, memory.getMaxValue() - element.getValue() -1, 1, 1);
			}
		}

	}

}

class SortingEvent {

	static interface Event{}

	static enum MemoryEvent implements Event{

		allocation,
		deallocation,
		move,
		remove,
		insert;
	}

	static enum ComparisonEvent implements Event{

		less,
		even,
		more;
	}

	static enum MarkingEvent implements Event{

		sorted,
		disregarded,
		available;
	}

	public final Event event;
	public SortingEvent(Event event) {
		this.event = event;
	}
}

class Element implements Comparable<Element> {
	
	static enum ElementState {
		unsorted(Color.red),
		sorted(Color.green),
		reading(Color.blue),
		removing(Color.gray),
		inserting(Color.white),
		comparing(Color.yellow);
		
		public final Color color;
		
		private ElementState(Color c) {
			color = c;
		}
	}

	static Element newInstance(int value){
		return new Element(value);
	}

	private final int value;
	private ElementState state = ElementState.unsorted;
	private Element(int value) {
		this.value = value;
	}
	@Override
	public int compareTo(Element o) {
		ElementState thisOldState = state;
		ElementState otherOldState = o.state;
		state = ElementState.comparing;
		o.state = ElementState.comparing;
		try {
			Thread.sleep(App.COMPARE_TIME);
		} catch (InterruptedException ex) {
		}
		state = thisOldState;
		o.state = otherOldState;
		
		int c = value - o.value;
		
		if (c < 0) return -1;
		else if (c == 0) return 0;
		else return 1;
	}

	public Element copy(){
		return new Element(value);
	}

	int getValue() {
		return value;
	}
	
	public void markSorted(){
		state = ElementState.sorted;
	}
	void setState(ElementState newState) {
		state = newState;
	}
	ElementState getState() {
		return state;
	}
}

class MemoryManager {

	static MemoryManager newInstance(int maxValue) {
		return new MemoryManager(maxValue);
	}

	private final List<MemoryArray> memory = new ArrayList<MemoryArray>();
	private final int maxValue;

	private MemoryManager(int maxValue) {
		this.maxValue = maxValue;
	}

	MemoryArray allocate(int size, String label) {
		final MemoryArray newMemory = MemoryArray.newInstance(size,
				maxValue, label);
		memory.add(newMemory);
		return newMemory;
	}

	void free(MemoryArray memoryArray) {
		memory.remove(memoryArray);
	}

}

class MemoryArray {

	private final int maxValue;
	
	private int reads = 0;
	private int removes = 0;
	private int writes = 0;
	private int swaps = 0;

	static MemoryArray newInstance(final int size, int maxValue, final
			String label) {
		return new MemoryArray(new Element[size], maxValue, label);
	}

	static MemoryArray randomInstance(final int size, final int maxValue,
			final String label) {
		final Random random = new Random();
		Element[] elements = new Element[size];
		for (int i = 0; i < elements.length; i++) {
			elements[i] = Element.newInstance(random.nextInt(maxValue + 1));
		}

		return new MemoryArray(elements, maxValue, label);
	}

	static MemoryArray shuffledInstance(final int size, final String
			label) {
		Element[] elements = new Element[size];
		for (int i = 0; i < elements.length; i++) {
			elements[i] = Element.newInstance(i);
		}
		Collections.shuffle(Arrays.asList(elements));
		return new MemoryArray(elements, size, label);
	}

	private final Element[] elements;
	private final String label;
	private MemoryArray(Element[] elements, int maxValue, String label) {
		this.elements = elements;
		this.maxValue = maxValue;
		this.label = label;
	}

	public void shuffle() {
		reads = 0;
		writes = 0;
		swaps = 0;
		removes = 0;
		
		Collections.shuffle(Arrays.asList(elements));
		for (Element e: elements) {
			e.setState(Element.ElementState.unsorted);
		}
	}
	
	public void insert(Element element, int index) {
		if (element == null)
			throw new NullPointerException("Element cannot be null when inserting");
		if (elements[index] != null) {
			throw new IllegalArgumentException(String.format("Index %d already occupied. Remove or swap to insert into occupied indexes", index));
		}
		if (element.getValue() > maxValue)
			throw new IllegalArgumentException(String.format("Element value is too high for this memory array. value: %d, maxValue: %d",
					element.getValue(), maxValue));
		elements[index] = element;
		Element.ElementState oldState = element.getState();
		element.setState(Element.ElementState.inserting);
		
		writes++;
		
		try {
			Thread.sleep(App.WRITE_TIME);
		} catch (InterruptedException ex) {
		}
		element.setState(oldState);
	}

	public Element read(int index) {
		Element element = elements[index];
		Element.ElementState oldState = element.getState();
		element.setState(Element.ElementState.reading);
		
		reads++;
		
		try {
			Thread.sleep(App.READ_TIME);
		} catch (InterruptedException ex) {
		}
		element.setState(oldState);
		
		return element;
	}

	public Element remove(int index) {
		Element element = elements[index];
		Element.ElementState oldState = element.getState();
		element.setState(Element.ElementState.removing);
		
		removes++;
		
		try {
			Thread.sleep(App.REMOVE_TIME);
		} catch (InterruptedException ex) {
		}
		elements[index] = null;
		element.setState(oldState);
		
		return element;
	}

	public int getSize() {
		return elements.length;
	}
	public String getLabel() {
		return label;
	}

	List<Element> getElements() {
		return Collections.unmodifiableList(Arrays.asList(elements));
	}

	int getMaxValue() {
		return maxValue;
	}

	public void swap(int i, int j) {
		Element element1 = elements[i];
		Element.ElementState oldState1 = element1.getState();
		element1.setState(Element.ElementState.inserting);
		
		Element element2 = elements[j];
		Element.ElementState oldState2 = element2.getState();
		element2.setState(Element.ElementState.inserting);
		
		swaps++;
		
		try {
			Thread.sleep(App.SWAP_TIME);
		} catch (InterruptedException ex) {
		}
		
		elements[i] = element2;
		elements[j] = element1;
		
		element1.setState(oldState1);
		element2.setState(oldState2);
	}

}

interface SortingAlgorithm {

	void sort(MemoryArray input, MemoryManager memoryManager);

}

class StoogeSort implements SortingAlgorithm {
	@Override
	public void sort(MemoryArray input, MemoryManager memoryManager) {
		sort(input, 0, input.getSize());
	}

	private void sort(MemoryArray input, final int start, final int end) {
		if (start >= end) return;
		
		if (input.read(start).compareTo(input.read(end-1)) > 0) {
			input.swap(start, end-1);
		}
		
		if (end - start > 2) {
			int t = (end-start)/3;
			sort(input, start, end - t);
			sort(input, start +t, end);
			sort(input, start, end - t);
		}
	}
}

class BubbleSort implements SortingAlgorithm {
	@Override
	public void sort(MemoryArray input, MemoryManager memoryManager) {
		for (int max = input.getSize(); max > 0; max--) {
			for (int i = 0; i < max - 1; i++) {
				if (input.read(i).compareTo(input.read(i+1)) > 0) {
					input.swap(i+1, i);
				}
			}
			input.read(max -1).markSorted();
		}
	}
}

class ShakerSort implements SortingAlgorithm {
	@Override
	public void sort(MemoryArray input, MemoryManager memoryManager) {
		int min = 0, max = input.getSize();
		boolean done = false;
		while (min < max && !done) {
			done = true;
			for (int i = min; i < max - 1; i++) {
				if (input.read(i).compareTo(input.read(i+1)) > 0) {
					input.swap(i+1, i);
					done = false;
				}
			}
			input.read(--max).markSorted();
			
			for (int i = max -1; i > min; i--) {
				if (input.read(i).compareTo(input.read(i-1)) < 0) {
					input.swap(i-1, i);
					done = false;
				}
			}
			input.read(min++).markSorted();
		}
		
		for (int i = min; i < max; i++) {
			input.read(i).markSorted();
		}
	}
}

class SelectionSort implements SortingAlgorithm {
	@Override
	public void sort(MemoryArray input, MemoryManager memoryManager) {
		for (int i = 0; i < input.getSize(); i++) {
			int min = i;
			Element minE = input.read(i);
			for (int j = i+1; j < input.getSize(); j++) {
				if (minE.compareTo(input.read(j)) >= 0) {
					min = j;
					minE = input.read(min);
				}
			}
			if (min != i)
				input.swap(i, min);
			minE.markSorted();
		}
	}

}

class InsertionSort implements SortingAlgorithm {
	@Override
	public void sort(MemoryArray input, MemoryManager memoryManager) {
		for (int i = 1; i < input.getSize(); i++) {
			int j = i-1;
			while (j >= 0 && input.read(j).compareTo(input.read(j+1)) > 0)
				input.swap(j+1, j--);
		}
	}
}

class ShellSort implements SortingAlgorithm {
	@Override
	public void sort(MemoryArray input, MemoryManager memoryManager) {
		int inc = input.getSize()/2;
		while (inc > 0) {
			for (int i = inc; i < input.getSize(); i++) {
				for (int j = i; j >= inc && input.read(j).compareTo(input.read(j-inc)) < 0; j -= inc) {
					input.swap(j, j - inc);
				}
			}
			if (inc > 2)
				inc /= 2.2;
			else
				inc--;
		}
		
	}
}

class HeapSort implements SortingAlgorithm {
	@Override
	public void sort(MemoryArray input, MemoryManager memoryManager) {
		for (int i = input.getSize()/2; i >= 0; i--) {
			downheap(input, input.getSize(), i);
		}
		
		for (int i = input.getSize() -1; i > 0; i--) {
			input.swap(0, i);
			input.read(i).markSorted();
			downheap(input, i, 0);
		}
		input.read(0).markSorted();
	}
	
	private void downheap(MemoryArray input, int size, int i) {
		if ((i+1)*2 -1 >= size) return;
		
		int t;
		if ((i+1)*2 >= size || input.read((i+1)*2 -1).compareTo(input.read((i+1)*2)) > 0)
			t = (i+1)*2 -1;
		else
			t = (i+1)*2;
		
		if (input.read(t).compareTo(input.read(i)) > 0) {
			input.swap(i, t);
			downheap(input, size, t);
		}
	}
}

class MergeSort implements SortingAlgorithm {
	@Override
	public void sort(MemoryArray input, MemoryManager memoryManager) {
		sort(input, memoryManager, 0, input.getSize());
	}
	
	private void sort(MemoryArray input, MemoryManager manager, final int start, final int end) {
		if (start >= end -1) return;
		
		int p = (start+end)/2;
		sort(input, manager, start, p);
		sort(input, manager, p, end);
		
		MemoryArray A = manager.allocate(p-start, "A");
		MemoryArray B = manager.allocate(end-p, "B");
		
		for (int i = 0; i < A.getSize(); i++) {
			A.insert(input.remove(start+i), i);
		}
		for (int i = 0; i < B.getSize(); i++) {
			B.insert(input.remove(p+i), i);
		}
		
		int i = 0, j = 0;
		
		while (i < A.getSize() && j < B.getSize()) {
			if (A.read(i).compareTo(B.read(j)) < 0) {
				input.insert(A.remove(i), start+i+j);
				i++;
			} else {
				input.insert(B.remove(j), start+i+j);
				j++;
			}
		}
		
		while (i < A.getSize()) {
			input.insert(A.remove(i), start+i+j);
			i++;
		}
		while (j < B.getSize()) {
			input.insert(B.remove(j), start+i+j);
			j++;
		}
		
		manager.free(A);
		manager.free(B);
	}
}

class QuickSort implements SortingAlgorithm {
	@Override
	public void sort(MemoryArray input, MemoryManager memoryManager) {
		sort(input, 0, input.getSize());
	}

	private void sort(MemoryArray input, final int start, final int end) {
		if (start >= end) return;
		Element pivot = input.remove(start);
		
		int spot = start;
		int direction = -1;
		
		int min = start +1;
		int max = end -1;
		int index = max;
		while (index != spot) {
			if (input.read(index).compareTo(pivot) == direction) {
				input.insert(input.remove(index), spot);
				spot = index;
				if (direction == -1) {
					max = index-1;
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
		sort(input, start, spot);
		sort(input, spot+1, end);
	}
	
}


class ParallellQuickSort implements SortingAlgorithm {
	
	private ExecutorService executor;
	
	@Override
	public void sort(MemoryArray input, MemoryManager memoryManager) {
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
		Counter c = new Counter(input.getSize());
		executor = new ThreadPoolExecutor(App.THREAD_POOL_SIZE, App.THREAD_POOL_SIZE, 1, TimeUnit.SECONDS, queue);
		sort(input, 0, input.getSize(), c);
		c.waitZero();
	}

	private void sort(final MemoryArray input, final int start, final int end, final Counter c) {
		switch (end-start) {
		case 0:
			return;
		case 1:
			input.read(start).markSorted();
			c.dec();
			return;
		case 2:
			if (input.read(start).compareTo(input.read(start+1)) > 0) {
				input.swap(start, start+1);
			}
			input.read(start).markSorted();
			c.dec();
			input.read(start +1).markSorted();
			c.dec();
			return;
		default:
		}
		
		Element pivot = input.remove(start);
		
		int spot = start;
		int direction = -1;
		
		int min = start +1;
		int max = end -1;
		int index = max;
		while (index != spot) {
			if (input.read(index).compareTo(pivot) == direction) {
				input.insert(input.remove(index), spot);
				spot = index;
				if (direction == -1) {
					max = index-1;
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
		if (end-start > 0) {
			executor.execute(new Runnable(){public void run(){sort(input, start, firstSpot, c);}});
			executor.execute(new Runnable(){public void run(){sort(input, firstSpot+1, end, c);}});
		} else {
			sort(input, start, firstSpot, c);
			sort(input, firstSpot+1, end, c);
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
			n--; notifyAll();
		}
	}
	
}

