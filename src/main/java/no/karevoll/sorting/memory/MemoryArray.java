package no.karevoll.sorting.memory;

public interface MemoryArray {

    public void insert(Element element, int index);

    public Element read(int index);

    public int getSize();

    public void markRemoved(int index);

    public void markSorted(int index);

}
