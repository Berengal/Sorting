package no.karevoll.sorting.memory;

public class MemorySlice implements MemoryArray {

    private MemoryArray memory;
    private int start;
    private int end;

    public MemorySlice(MemoryArray input) {
	this.memory = input;
	this.start = 0;
	this.end = input.getSize();
    }

    public MemorySlice(MemoryArray input, int start, int end) {
	if (start < 0) {
	    throw new IndexOutOfBoundsException();
	}
	if (end > input.getSize()) {
	    throw new IndexOutOfBoundsException();
	}
	if (start > end) {
	    throw new IllegalArgumentException();
	}

	this.memory = input;
	this.start = start;
	this.end = end;
    }

    public MemorySlice sliceLeft(int index) {
	if (index == getSize()) {
	    return this;
	}
	return new MemorySlice(memory, start, convertEx(index));
    }

    public MemorySlice sliceRight(int index) {
	if (index == 0) {
	    return this;
	}
	return new MemorySlice(memory, convertEx(index), end);
    }

    @Override
    public int getSize() {
	return end - start;
    }

    private int convert(int index) {
	if (index < 0) {
	    throw new ArrayIndexOutOfBoundsException(index);
	}
	if (index >= getSize()) {
	    throw new ArrayIndexOutOfBoundsException(index);
	}
	return index + start;
    }

    private int convertEx(int index) {
	if (index < 0) {
	    throw new ArrayIndexOutOfBoundsException(index);
	}
	if (index > getSize()) {
	    throw new ArrayIndexOutOfBoundsException(index);
	}
	return index + start;
    }

    @Override
    public void insert(Element element, int index) {
	memory.insert(element, convert(index));
    }

    public void set(Element element, int index) {
	int i = convert(index);
	memory.markRemoved(i);
	memory.insert(element, i);
    }

    @Override
    public Element remove(int index) {
	int i = convert(index);
	Element e = memory.read(i);
	memory.markRemoved(i);
	return e;
    }

    @Override
    public void swap(int i, int j) {
	if (i != j) {
	    int p = convert(i);
	    int q = convert(j);

	    Element a = memory.read(p);
	    Element b = memory.read(q);
	    set(a, j);
	    set(b, i);
	}
    }

    @Override
    public void markRemoved(int index) {
	memory.markRemoved(convert(index));
    }

    public int compare(int i, int j) {
	return compare(i, memory.read(convert(j)));
    }

    public int compare(int i, Element e) {
	return memory.read(convert(i)).compareTo(e);
    }

    public boolean compareAndSwap(int i, int j) {
	int p = convert(i);
	int q = convert(j);
	Element a = memory.read(p);
	Element b = memory.read(q);

	if (a.compareTo(b) > 0) {
	    memory.markRemoved(p);
	    memory.markRemoved(q);
	    memory.insert(a, q);
	    memory.insert(b, p);
	    return true;
	} else {
	    return false;
	}
    }

    public void markSorted(int index) {
	memory.read(convert(index)).markSorted();
    }

    public void markSorted() {
	for (int i = start; i < end; i++) {
	    memory.read(i).markSorted();
	}
    }

    @Override
    public Element read(int index) {
	return memory.read(convert(index));
    }

}
