package queue;

public class ArrayQueue {
	private int size = 0, start = 0;
	private Object[] elements = new Object[4];

	//Invariant: elements != null
	//			&& for i in range(0, min(size', size)) q'[i] == q[i]
	//			&& 0 <= end < elements.length
	//			&& 0 <= start < elements.length
	//			&& 0 <= size <= elements.length

	private int end() {
		return ((start + size - 1) % elements.length);
	}

	//Pre: -
	private void ensureCapacity(int capacity) {
		if (elements.length >= capacity) {
			return;
		}

		Object[] newElements = new Object[2 * capacity];
		
		if (start < end()) {
			System.arraycopy(elements, start, newElements, start, end() - start + 1);
		}
		else {
			System.arraycopy(elements, start, newElements, start, elements.length - start);
			System.arraycopy(elements, 0, newElements, elements.length, end() + 1);
		}

		elements = newElements;
	}
	//Post: elements'.length == 2 * capacity
	//		&& start' == end + size
	//		&& end' == end
	//		&& size' == size

	//Pre: index >= 0
	//		&& size > 0
	private int nextIndex(int index, int size) {
		return ((index + 1) % size);
	}
	//Post: R == index' == (index + 1) % size
	//		&& size' == size

	//Pre: index >= 0
	//		&& size > 0
	private int previousIndex(int index, int size) {
		return ((index + size - 1) % size);
	}
	//Post: R == index' == (index + size - 1) % size
	//		&& size' == size

	//Pre: element != null
	public void enqueue(Object element) {
		assert element != null;

		ensureCapacity(size + 1);
		size++;
		elements[end()] = element;
	}
	//Post: elements'.length >= size + 1
	//		&& elements'.length >= elements.length
	//		&& end' == (end + 1) % elements'.length
	//		&& size' == size + 1
	//		&& start' == start 

	//Pre: size != 0
	public Object element() {
		assert size > 0;

		return elements[start];
	}
	//Post: R == elements[start]
	//		&& size' == size
	//		&& end' == end
	//		&& start' == start
	//		&& elements'.length == elements.length

	//Pre: size != 0
	public Object dequeue() {
		assert size > 0;

		Object result = elements[start];
		start = nextIndex(start, elements.length);
		size--;
		return result;
	}
	//Post: R == elements[start]
	//		&& size' == size - 1
	//		&& end' == end
	//		&& start' == (start + 1) % elements.length
	//		&& elements'.length == elements.length 

	//--- Begin modification

	//Pre: element != null
	public void push(Object element) {
		assert element != null;

		ensureCapacity(size + 1);
		start = previousIndex(start, elements.length);
		size++;
		elements[start] = element;
	}
	//Post: elements'.length >= size + 1
	//		&& elements'.length >= elements.length
	//		&& start' == (start + elements'.length - 1) % elements'.length
	//		&& size' == size + 1
	//		&& end' == end

	//Pre: size != 0
	public Object peek() {
		assert size > 0;

		return elements[end()];
	}
	//Post: R == elements[end]
	//		&& size' == size
	//		&& end' == end
	//		&& start' == start
	//		&& elements'.length == elements.length

	//Pre: size != 0
	public Object remove() {
		assert size > 0;

		Object result = elements[end()];
		size--;
		return result;
	}
	//Post: R == elements[end']
	//		&& size' == size - 1
	//		&& start' == start
	//		&& end' == (end + elements.length - 1) % elements.length
	//		&& elements'.length == elements.length

	//--- start modification

	//Pre: -
	public int size() {
		return size;
	}
	//Post: R == size
	//		&& size' == size
	//		&& elements'.length == elements.length
	//		&& end' == end
	//		&& start' == start

	//Pre: -
	public boolean isEmpty() {
		return size == 0;
	}
	//Post: R == (size == 0 ? true : false)
	//		&& size' == size
	//		&& elements'.length == elements.length
	//		&& end' == end
	//		&& start' == end

	//Pre: -
	public void clear() {
		start = size = 0;
	}
	//Post: size' == 0
	//		&& elements'.length == elements.length
	//		&& end' == 0
	//		&& start' == 0

	public void main(String[] args) {
	
	}
}