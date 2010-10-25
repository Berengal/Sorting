package no.karevoll.sorting.memory;

public interface MemoryArray {

    public void insert(Element element, int index);

    public Element read(int index);

    @Deprecated
    public Element remove(int index);

    public int getSize();

    @Deprecated
    public void swap(int i, int j);

    public void markRemoved(int index);

}
