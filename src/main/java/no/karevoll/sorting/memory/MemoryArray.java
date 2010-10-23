/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.karevoll.sorting.memory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import no.karevoll.sorting.App;

public class MemoryArray {

    private final int maxValue;
    private int reads = 0;
    private int removes = 0;
    private int writes = 0;
    private int swaps = 0;

    static MemoryArray newInstance(final int size, int maxValue,
	    final String label) {
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

    public static MemoryArray shuffledInstance(final int size,
	    final String label) {
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
	for (Element e : elements) {
	    e.setState(Element.ElementState.unsorted);
	}
    }

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
	if (element.getValue() > maxValue) {
	    throw new IllegalArgumentException(
		    String
			    .format(
				    "Element value is too high for this memory array. value: %d, maxValue: %d",
				    element.getValue(), maxValue));
	}
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

    public int getReads() {
	return reads;
    }

    public int getWrites() {
	return writes;
    }

    public int getRemoves() {
	return removes;
    }

    public int getSwaps() {
	return swaps;
    }

}
