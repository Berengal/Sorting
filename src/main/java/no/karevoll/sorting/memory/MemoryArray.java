package no.karevoll.sorting.memory;

public interface MemoryArray {

    public void insert(Element element, int index);

    public Element read(int index);

    public Element remove(int index);

    public int getSize();

    public void swap(int i, int j);

    public void markRemoved(int index);

}
