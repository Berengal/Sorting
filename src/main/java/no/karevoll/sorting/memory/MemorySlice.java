package no.karevoll.sorting.memory;

public class MemorySlice implements MemoryArray {

    private MemoryArray memory;
    private int start;
    private int end;

    public MemorySlice(MemoryArray input) {
	this(input, 0, input.getSize());
    }

    public MemorySlice(MemoryArray input, int start, int end) {
	this.memory = input;
	this.start = start;
	this.end = end;
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

    @Override
    public void insert(Element element, int index) {
	memory.insert(element, convert(index));
    }

    @Override
    public Element read(int index) {
	return memory.read(convert(index));
    }

    @Override
    public Element remove(int index) {
	return memory.remove(convert(index));
    }

    @Override
    public void swap(int i, int j) {
	memory.swap(convert(i), convert(j));
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

}
