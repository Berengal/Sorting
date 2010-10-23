/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.karevoll.sorting.memory;

import java.awt.Color;
import no.karevoll.sorting.App;

/**
 *
 * @author berengal
 */
public class Element implements Comparable<Element> {

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