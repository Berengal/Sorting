package no.karevoll.sorting.memory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import no.karevoll.sorting.Counter;
import no.karevoll.sorting.Settings;

public class MemoryArrayImpl implements MemoryArray {

    private final Settings settings;
    private Counter counter;

    static MemoryArrayImpl newInstance(final int size, final Settings settings,
	    Counter counter, final String label) {
	return new MemoryArrayImpl(new Element[size], settings, counter, label);
    }

    static MemoryArrayImpl randomInstance(final Settings settings,
	    Counter counter, final String label) {
	final Random random = new Random();
	Element[] elements = new Element[settings.LIST_SIZE];
	for (int i = 0; i < elements.length; i++) {
	    elements[i] = Element.newInstance(random
		    .nextInt(settings.MAX_VALUE + 1), counter);
	}

	return new MemoryArrayImpl(elements, settings, counter, label);
    }

    public static MemoryArrayImpl shuffledInstance(final Settings settings,
	    Counter counter, final String label) {
	Element[] elements = new Element[settings.LIST_SIZE];
	for (int i = 0; i < elements.length; i++) {
	    elements[i] = Element.newInstance(i, counter);
	}
	Collections.shuffle(Arrays.asList(elements));
	return new MemoryArrayImpl(elements, settings, counter, label);
    }

    private final Element[] elements;
    private final String label;

    private MemoryArrayImpl(Element[] elements, Settings settings,
	    Counter counter, String label) {
	this.elements = elements;
	this.settings = settings;
	this.counter = counter;
	this.label = label;
    }

    @Override
    public void insert(Element element, int index) {
	if (element == null) {
	    throw new NullPointerException(
		    "Element cannot be null when inserting");
	}
	if (elements[index] != null) {
	    throw new IllegalArgumentException(
		    String
			    .format(
				    "Index %d already occupied. Remove or swap to insert into occupied indexes",
				    index));
	}
	if (!element.hasCounter(counter)) {
	    throw new IllegalArgumentException("Element not part of dataset.");
	}
	elements[index] = element;
	Element.ElementState oldState = element.getState();
	element.setState(Element.ElementState.inserting);

	counter.write();

	element.setState(oldState);
    }

    @Override
    public Element read(int index) {
	Element element = elements[index];
	Element.ElementState oldState = element.getState();
	element.setState(Element.ElementState.reading);

	counter.read();
	element.setState(oldState);

	return element;
    }

    @Override
    public int getSize() {
	return elements.length;
    }

    public String getLabel() {
	return label;
    }

    public List<Element> getElements() {
	return Collections.unmodifiableList(Arrays.asList(elements));
    }

    @Override
    public void markRemoved(int index) {
	Element element = elements[index];
	Element.ElementState oldState = element.getState();
	element.setState(Element.ElementState.removing);

	counter.remove();
	elements[index] = null;
	element.setState(oldState);
    }

    public boolean isSorted() {
	boolean r = elements[0].getState() == Element.ElementState.sorted;
	for (int i = 1; r && i < elements.length; i++) {
	    r &= elements[i - 1].compareTo(elements[i]) <= 0;
	    r &= elements[i].getState() == Element.ElementState.sorted;
	}
	return r;
    }

    @Override
    public void markSorted(int index) {
	Element element = elements[index];
	element.markSorted();
    }
}
