package no.karevoll.sorting.memory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import no.karevoll.sorting.App;

public class MemoryArrayImpl implements MemoryArray {

  private final int maxValue;
  private int reads = 0;
  private int removes = 0;
  private int writes = 0;
  private int swaps = 0;
  static MemoryArrayImpl newInstance(final int size, int maxValue,
          final String label) {
    return new MemoryArrayImpl(new Element[size], maxValue, label);
  }

  static MemoryArrayImpl randomInstance(final int size, final int maxValue,
          final String label) {
    final Random random = new Random();
    Element[] elements = new Element[size];
    for (int i = 0; i < elements.length; i++) {
      elements[i] = Element.newInstance(random.nextInt(maxValue + 1));
    }

    return new MemoryArrayImpl(elements, maxValue, label);
  }

  public static MemoryArrayImpl shuffledInstance(final int size,
          final String label) {
    Element[] elements = new Element[size];
    for (int i = 0; i < elements.length; i++) {
      elements[i] = Element.newInstance(i);
    }
    Collections.shuffle(Arrays.asList(elements));
    return new MemoryArrayImpl(elements, size, label);
  }

  private final Element[] elements;
  private final String label;
  private MemoryArrayImpl(Element[] elements, int maxValue, String label) {
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

  @Override
  public void insert(Element element, int index) {
    if (element == null) {
      throw new NullPointerException(
              "Element cannot be null when inserting");
    }
    if (elements[index] != null) {
      throw new IllegalArgumentException(
              String.format(
              "Index %d already occupied. Remove or swap to insert into occupied indexes",
              index));
    }
    if (element.getValue() > maxValue) {
      throw new IllegalArgumentException(
              String.format(
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

  @Override
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

  @Override
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

  public int getMaxValue() {
    return maxValue;
  }

  @Override
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

  @Override
  public void markRemoved(int index) {
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
  }

  public boolean isSorted() {
    boolean r = true;
    for (int i = 1; r && i < elements.length; i++) {
      r &= elements[i - 1].compareTo(elements[i]) <= 0;
    }
    return r;
  }

}
