package queue;

import java.util.function.*;

public class ArrayQueue extends AbstractQueue {
  private int size = 0, start = 0;
  private Object[] elements = new Object[4];

  private int end() {
    return ((start + size - 1) % elements.length);
  }

  private void ensureCapacity(int capacity) {
    if (elements.length >= capacity) {
      return;
    }

    Object[] newElements = new Object[2 * capacity];

    if (start < end()) {
      System.arraycopy(elements, start, newElements, start, end() - start + 1);
    } else {
      System.arraycopy(elements, start, newElements, start, elements.length - start);
      System.arraycopy(elements, 0, newElements, elements.length, end() + 1);
    }

    elements = newElements;
  }

  private int nextIndex(int index, int size) {
    return ((index + 1) % size);
  }

  public void enqueueImpl(Object element) {
    ensureCapacity(size + 1);
    size++;
    elements[end()] = element;
    size--;
  }

  public Object elementImpl() {
    return elements[start];
  }

  public void removeImpl() {
    start = nextIndex(start, elements.length);
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public void clear() {
    start = size = 0;
  }

  public ArrayQueue makeQueue() {
    return new ArrayQueue();
  }

  public static void main(String[] args) {

  }
}