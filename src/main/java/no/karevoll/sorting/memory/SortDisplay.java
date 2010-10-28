package no.karevoll.sorting.memory;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import no.karevoll.sorting.Settings;
import no.karevoll.sorting.SortingEvent;

public class SortDisplay extends Component {

    private static final long serialVersionUID = 2775060975992246909L;

    public static SortDisplay getInstance(MemoryArrayImpl memory,
	    Settings settings) {
	return new SortDisplay(memory, settings);
    }

    private Settings settings;
    private final MemoryArrayImpl memory;
    private final Queue<SortingEvent.Event> eventQueue = new ConcurrentLinkedQueue<SortingEvent.Event>();
    {
	setBackground(Color.black);
    }

    private SortDisplay(MemoryArrayImpl memory, Settings settings) {
	this.memory = memory;
	if (!settings.FULLSCREEN)
	    setPreferredSize(new Dimension(settings.FRAME_WIDTH,
		    settings.FRAME_HEIGHT));
	this.settings = settings;
    }

    @Override
    public void paint(Graphics graphics) {
	Graphics2D g = (Graphics2D) graphics;
	g.setColor(Color.black);
	g.fill(g.getClip());

	Dimension memorySize = new Dimension(memory.getSize(),
		settings.MAX_VALUE);
	Dimension drawingSize = getSize();
	g.scale(drawingSize.getWidth() / memorySize.getWidth(), drawingSize
		.getHeight()
		/ memorySize.getHeight());
	final List<Element> elements = memory.getElements();
	for (int i = 0; i < elements.size(); i++) {
	    Element element = elements.get(i);
	    if (element != null) {
		g.setColor(element.getState().color);
		g
			.drawOval(i, settings.MAX_VALUE - element.getValue()
				- 1, 1, 1);
		// g.drawOval(element.getValue(), elements.size() - i + 1, 1,1);
	    }
	}

    }
}
