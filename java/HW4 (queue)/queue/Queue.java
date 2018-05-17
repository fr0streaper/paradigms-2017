package queue;

import java.util.function.*;

public interface Queue {

  //Pre: element != null
  void enqueue(Object element);
  //Post: for i in range(queue.size) queue'[i] == queue[i]
  //		&& queue'[queue'.end] == element
  //		&& queue'.size == queue.size + 1

  //Pre: queue != null
  Object element();
  //Post: R == queue[queue.start]
  //		&& queue' == queue
  //		&& queue'.size == queue.size

  //Pre: queue != null
  Object dequeue();
  //Post: R == queue[queue.start]
  //		&& queue'.size == queue.size - 1
  //		&& for i in range(queue'.size) queue'[i] == queue[i + 1]

  //Pre: -
  public int size();
  //Post: R == queue.size
  //		&& queue' == queue
  //		&& queue'.size == queue.size

  //Pre: -
  public void clear();
  //Post: queue' == null
  //      && queue'.size == 0

  //Pre: -
  public boolean isEmpty();
  //Post: R == (queue.size == 0 ? true : false)
  //		&& queue' == queue
  //		&& queue'.size == queue.size

  //Pre: predicate != null
  public Queue filter(Predicate<Object> predicate);
  //Post: queue' == queue
  //      && queue'.size == queue.size
  //      && R == (Queue){q : q in queue && predicate(q)}
  //      && predicate' == predicate

  //Pre: function != null
  public Queue map(Function<Object, Object> function);
  //Post: queue' == queue
  //      && queue'.size == queue.size
  //      && R == (Queue){function(q) : q in queue}
  //      && function' == function
}	