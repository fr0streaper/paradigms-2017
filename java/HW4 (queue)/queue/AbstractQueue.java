package queue;

import java.util.function.*;

public abstract class AbstractQueue implements Queue {
  protected int size;

  public void enqueue(Object element) {
    assert element != null;

    enqueueImpl(element);
    size++;
  }

  protected void enqueueImpl(Object element);

  public Object element() {
    assert size != 0;

    return elementImpl();
  }

  protected abstract Object elementImpl();

  public Object dequeue() {
    assert size != 0;

    Object result = element();
    removeImpl();
    size--;
    return result;
  }

  protected abstract void removeImpl();

  public abstract Queue makeQueue();

  public Queue filter(Predicate<Object> predicate) {
    Queue queue = makeQueue();

    for (int i = 0; i < size; i++) {
      Object current = dequeue();
      enqueue(current);

      if (predicate.test(current)) {
        queue.enqueue(current);
      }
    }

    return queue;
  }

  public Queue map(Function<Object, Object> function) {
    Queue queue = makeQueue();

    for (int i = 0; i < size; i++) {
      Object current = dequeue();
      enqueue(current);

      queue.enqueue(function.apply(current));
    }
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }
}