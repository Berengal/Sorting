package no.karevoll.sorting.memory;

import java.awt.Color;

import no.karevoll.sorting.Counter;

public class Element implements Comparable<Element> {
    private Counter counter;

    static enum ElementState {
	unsorted(Color.red), sorted(Color.green), reading(Color.blue), removing(
		Color.gray), inserting(Color.white), comparing(Color.yellow);

	public final Color color;

	private ElementState(Color c) {
	    color = c;
	}
    }

    static Element newInstance(int value, Counter counter) {
	return new Element(value, counter);
    }

    private final int value;
    private ElementState state = ElementState.unsorted;

    private Element(int value, Counter counter) {
	this.value = value;
	this.counter = counter;
    }

    @Override
    public int compareTo(Element o) {
	ElementState thisOldState = state;
	ElementState otherOldState = o.state;
	state = ElementState.comparing;
	o.state = ElementState.comparing;

	counter.compare();

	state = thisOldState;
	o.state = otherOldState;

	int c = value - o.value;

	if (c < 0)
	    return -1;
	else if (c == 0)
	    return 0;
	else
	    return 1;
    }

    public void markSorted() {
	state = ElementState.sorted;
    }

    void setState(ElementState newState) {
	state = newState;
    }

    ElementState getState() {
	return state;
    }

    public boolean hasCounter(Counter counter) {
	return this.counter == counter;
    }

    public int getValue() {
	return value;
    }
}