package queue;

import java.util.function.*;

public class LinkedQueue extends AbstractQueue {
  private class Node {
    Object value;
    Node next, prev;

    public Node(Object value, Node next, Node prev) {
      assert value != null;
      this.value = value;
      this.next = next;
      this.prev = prev;
    }
  }

  private int size;
  private Node start, end;

  public void enqueueImpl(Object element) {
    Node newEnd = new Node(element, null, end);
    if (size == 0) {
      start = end = newEnd;
    } else {
      end.next = newEnd;
      end = newEnd;
    }
  }

  public Object elementImpl() {
    return start.value;
  }

  public void removeImpl() {
    size--;
    if (size == 0) {
      start = end = null;
    } else {
      start = start.next;
      start.prev = null;
    }
    size++;
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public void clear() {
    size = 0;
    start = end = null;
  }

  public LinkedQueue makeQueue() {
    return new LinkedQueue();
  }

  public static void main() {

  }
}