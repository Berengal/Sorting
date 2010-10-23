/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.karevoll.sorting;

/**
 *
 * @author berengal
 */
public class SortingEvent {

  public static interface Event {
  }

	public static enum MemoryEvent implements Event{

		allocation,
		deallocation,
		move,
		remove,
		insert;
	}

  public static enum ComparisonEvent implements Event {

		less,
		even,
		more;
	}

  public static enum MarkingEvent implements Event {

		sorted,
		disregarded,
		available;
	}

	public final Event event;
	public SortingEvent(Event event) {
		this.event = event;
	}
}
