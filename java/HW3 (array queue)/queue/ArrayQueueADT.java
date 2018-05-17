package queue;

public class ArrayQueueADT {
	private int start = 0, size = 0;
	private Object[] elements = new Object[4];

	//Comment on the contract: in all pre- and post-conditions prefix "queue." 
	// for elements, size, start and end is omitted for easier understanding

	//Invariant: elements != null
	//			&& for i in range(0, min(size', size)) q'[i] == q[i]
	//			&& 0 <= end < elements.length
	//			&& 0 <= start < elements.length
	//			&& 0 <= size <= elements.length

	private static int end(ArrayQueueADT queue) {
		return ((queue.start + queue.size - 1) % queue.elements.length);
	}

	//Pre: -
	private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
		if (queue.elements.length >= capacity) {
			return;
		}

		Object[] newElements = new Object[2 * capacity];
		
		if (queue.start < end(queue)) {
			System.arraycopy(queue.elements, queue.start, newElements, queue.start, end(queue) - queue.start + 1);
		}
		else {
			System.arraycopy(queue.elements, queue.start, newElements, queue.start, queue.elements.length - queue.start);
			System.arraycopy(queue.elements, 0, newElements, queue.elements.length, end(queue) + 1);
		}

		queue.elements = newElements;
	}
	//Post: elements'.length == 2 * capacity
	//		&& start' == end + size
	//		&& end' == end
	//		&& size' == size

	//Pre: index >= 0
	//		&& size > 0
	private static int nextIndex(int index, int size) {
		return ((index + 1) % size);
	}
	//Post: R == index' == (index + 1) % size
	//		&& size' == size

	//Pre: index >= 0
	//		&& size > 0
	private static int previousIndex(int index, int size) {
		return ((index + size - 1) % size);
	}
	//Post: R == index' == (index + size - 1) % size
	//		&& size' == size

	//Pre: element != null
	public static void enqueue(ArrayQueueADT queue, Object element) {
		assert element != null;

		ensureCapacity(queue, queue.size + 1);
		queue.size++;
		queue.elements[end(queue)] = element;
	}
	//Post: elements'.length >= size + 1
	//		&& elements'.length >= elements.length
	//		&& end' == (end + 1) % elements'.length
	//		&& size' == size + 1
	//		&& start' == start 

	//Pre: size != 0
	public static Object element(ArrayQueueADT queue) {
		assert queue.size > 0;

		return queue.elements[queue.start];
	}
	//Post: R == elements[start]
	//		&& size' == size
	//		&& end' == end
	//		&& start' == start
	//		&& elements'.length == elements.length

	//Pre: size != 0
	public static Object dequeue(ArrayQueueADT queue) {
		assert queue.size > 0;

		Object result = queue.elements[queue.start];
		queue.start = nextIndex(queue.start, queue.elements.length);
		queue.size--;
		return result;
	}
	//Post: R == elements[start]
	//		&& size' == size - 1
	//		&& end' == end
	//		&& start' == (start + 1) % elements.length
	//		&& elements'.length == elements.length

	//--- Begin modification

	//Pre: element != null
	public static void push(ArrayQueueADT queue, Object element) {
		assert element != null;

		ensureCapacity(queue, queue.size + 1);
		queue.start = previousIndex(queue.start, queue.elements.length);
		queue.size++;
		queue.elements[queue.start] = element;
	}
	//Post: elements'.length >= size + 1
	//		&& elements'.length >= elements.length
	//		&& start' == (start + elements'.length - 1) % elements'.length
	//		&& size' == size + 1
	//		&& end' == end

	//Pre: size != 0
	public static Object peek(ArrayQueueADT queue) {
		assert queue.size > 0;

		return queue.elements[end(queue)];
	}
	//Post: R == elements[end]
	//		&& size' == size
	//		&& end' == end
	//		&& start' == start
	//		&& elements'.length == elements.length

	//Pre: size != 0
	public static Object remove(ArrayQueueADT queue) {
		assert queue.size > 0;

		Object result = queue.elements[end(queue)];
		queue.size--;
		return result;
	}
	//Post: R == elements[end']
	//		&& size' == size - 1
	//		&& start' == start
	//		&& end' == (end + elements.length - 1) % elements.length
	//		&& elements'.length == elements.length

	//--- start modification

	//Pre: -
	public static int size(ArrayQueueADT queue) {
		return queue.size;
	}
	//Post: R == size
	//		&& size' == size
	//		&& elements'.length == elements.length
	//		&& end' == end
	//		&& start' == start

	//Pre: -
	public static boolean isEmpty(ArrayQueueADT queue) {
		return queue.size == 0;
	}
	//Post: R == (size == 0 ? true : false)
	//		&& size' == size
	//		&& elements'.length == elements.length
	//		&& end' == end
	//		&& start' == end

	//Pre: -
	public static void clear(ArrayQueueADT queue) {
		queue.start = queue.size = 0;
	}
	//Post: size' == 0
	//		&& elements'.length == elements.length
	//		&& end' == 0
	//		&& start' == 0

	public static void main(String[] args) {
	
	}
}