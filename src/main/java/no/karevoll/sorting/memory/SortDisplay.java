package no.karevoll.sorting.memory;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import no.karevoll.sorting.SortingEvent;

public class SortDisplay extends Component {
    private static final long serialVersionUID = 2775060975992246909L;

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
	setPreferredSize(new Dimension(memory.getSize(), memory.getMaxValue()));
    }

    @Override
    public void paint(Graphics graphics) {
	Graphics2D g = (Graphics2D) graphics;
	g.setColor(Color.black);
	g.fill(g.getClip());

	Dimension memorySize = new Dimension(memory.getSize(), memory
		.getMaxValue());
	Dimension drawingSize = getSize();
	g.scale(drawingSize.getWidth() / memorySize.getWidth(), drawingSize
		.getHeight()
		/ memorySize.getHeight());

	final List<Element> elements = memory.getElements();
	for (int i = 0; i < elements.size(); i++) {
	    Element element = elements.get(i);
	    if (element != null) {
		g.setColor(element.getState().color);
		g.drawOval(i, memory.getMaxValue() - element.getValue() - 1, 1,
			1);
	    }
	}

    }

}
