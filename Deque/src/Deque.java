import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 
 * @author ADMIN Implement deque (Double-ended queue) in Java
 * @param <T>
 */
@SuppressWarnings("unchecked")
public class Deque<T> implements Iterable<T> {
	private T[] block; // Block data
	private int frontIndex; // Front index, use for some operation in front deque.
	private int backIndex; // Back index, use for some operation in back deque.
	private int size; // Number of element in deque

	/**
	 * Construct deque with default capacity is 32
	 */
	public Deque() {
		this(32);
	}

	/**
	 * Construct deque with specific capacity This capacity must be positive (or
	 * equal to 0)
	 * 
	 * @param capacity The specific capacity when call constructor.
	 */
	public Deque(int capacity) {
		if (capacity < 0)
			throw new IllegalArgumentException("Illegal capacity");
		this.frontIndex = this.backIndex = this.size = 0;
		this.block = (T[]) new Object[capacity];
	}

	/**
	 * Clear all element in deque This method just clear data, no resize
	 */
	public void clear() {
		for (int i = 0; i < capacity(); ++i)
			this.block[i] = null;
		this.frontIndex = this.backIndex = this.size = 0;
	}

	/**
	 * Return the size of deque
	 * 
	 * @return Size (or number of element) in deque.
	 */
	public int size() {
		return this.size;
	}

	/**
	 * Return the capacity of deque
	 * 
	 * @return Capacity (the max number of memory block) in deque.
	 */
	public int capacity() {
		return this.block.length;
	}

	/**
	 * Add object to deque This method will add object to last position in the
	 * deque, When the deque is full (i.e the size equal to the capacity), it will
	 * return false to notify the deque is full and you can't add any object
	 * 
	 * @param object Object for add
	 * @return true if add successful, otherwise return false
	 */
	public boolean add(T object) {
		// Check size equal to capacity
		if (size == capacity())
			return false; // Return false if the deque is full
		// Call private method add object which type is 1 to add object
		// in the last position deque
		addObject(object, 1);
		return true;
	}

	/**
	 * Add object to deque This method will add element to last position in the
	 * deque, When the deque is full (i.e the size equal to the capacity), it will
	 * auto resize the block, so you can call this method any time you want
	 * 
	 * @param object Object for add
	 */
	public void addLast(T object) {
		// Call private method add object which type is 1 to add object
		// in the last position deque
		addObject(object, 1);
	}

	/**
	 * Add object to deque This method will add object to first position in the
	 * deque, When the deque is full (i.e the size equal to the capacity), it will
	 * auto resize the block, so you can call this method any time you want
	 * 
	 * @param object Object for add
	 */
	public void addFirst(T object) {
		// Call private method add object which type is 0 to add object
		// in the first position deque
		addObject(object, 0);
	}

	/**
	 * Remove and retrieve the first object in deque This method will return null if
	 * the deque is empty
	 * 
	 * @return first object is deque, null if the deque is empty
	 */
	public T popFirst() {
		// Call private method pop object which type is 0 to remove
		// and retrieve first object in deque
		return popObject(0);
	}

	/**
	 * Remove and retrieve the last object in deque This method will return null if
	 * the deque is empty
	 * 
	 * @return last object is deque, null if the deque is empty
	 */
	public T popLast() {
		// Call private method pop object which type is 1 to remove
		// and retrieve last object in deque
		return popObject(1);
	}

	/**
	 * Retrieve the first object in deque This method will return null if the deque
	 * is empty
	 * 
	 * @return first object is deque, null if the deque is empty
	 */
	public T peekFirst() {
		// Call private method peek object which type is 0 to
		// retrieve first object in deque
		return peekObject(0);
	}

	/**
	 * Retrieve the last object in deque This method will return null if the deque
	 * is empty
	 * 
	 * @return last object is deque, null if the deque is empty
	 */
	public T peekLast() {
		// Call private method peek object which type is 1 to
		// retrieve last object in deque
		return peekObject(1);
	}

	/**
	 * Private function to add new object in deque
	 * 
	 * @param object   Object for add
	 * @param typePush Type for add, 0: add to the first position, 1: add to the
	 *                 last position
	 */
	private void addObject(T object, int typePush) {
		if (typePush < 0 || typePush > 1) // Throw if type doesn't match
			throw new IllegalArgumentException("Invalid type push");
		if (object == null) // Validate object is null
			throw new IllegalArgumentException("Don't support null object");
		if (this.size == capacity()) // Check size is equal to capacity
			resizeDeque(Math.max(size + 1, capacity() * 2)); // Resize deque
		if (typePush == 0) {
			--frontIndex;
			if (frontIndex < 0)
				frontIndex = capacity() - 1;
			block[frontIndex] = object;
		} else {
			block[backIndex++] = object;
			if (backIndex == capacity())
				backIndex = 0;
		}
		++size;
	}

	/**
	 * Private method to remove and retrieve the first or the last object in deque
	 * if the deque is empty, return null
	 * 
	 * @param typePop // Type for remove, 0: remove and retrieve first object in
	 *                deque, 1: remove and retrieve last object in deque
	 * @return first or last object in deque depend on typePop argument, null if the
	 *         deque is empty
	 */
	private T popObject(int typePop) {
		if (typePop < 0 || typePop > 1)
			throw new IllegalArgumentException("Invalid type pop"); // Throw if type doesn't match
		T object = null;
		if (size != 0) {
			if (typePop == 0) {
				object = block[frontIndex];
				block[frontIndex++] = null;
				if (frontIndex == capacity())
					frontIndex = 0;
			} else {
				--backIndex;
				if (backIndex < 0)
					backIndex = capacity() - 1;
				object = block[backIndex];
				block[backIndex] = null;
			}
			--size;
		}
		return object;
	}

	/**
	 * Private method to retrieve the first or the last object in deque if the deque
	 * is empty, return null
	 * 
	 * @param typePop // Type for retrieve, 0: retrieve first object in deque, 1:
	 *                retrieve last object in deque
	 * @return first or last object in deque depend on typePeek argument, null if
	 *         the deque is empty
	 */
	private T peekObject(int typePeek) {
		if (typePeek < 0 || typePeek > 1)
			throw new IllegalArgumentException("Invalid type pop");
		T object = null;
		if (size != 0)
			if (typePeek == 0)
				object = block[frontIndex];
			else {
				if (backIndex - 1 < 0)
					object = block[capacity() - 1];
				else
					object = block[backIndex - 1];
			}

		return object;
	}

	/**
	 * Method to resize the deque
	 * 
	 * @param newCapacity new capacity to resize
	 */
	private void resizeDeque(int newCapacity) {
		// If new capacity is greater than old capacity, resize
		if (newCapacity > capacity()) {
			T[] newBlock = (T[]) new Object[newCapacity]; // Allocate new block
			if (size > 0) {
				// Copy data if size > 0
				for (int i = 0; i < backIndex; ++i)
					newBlock[i] = block[i];
				int oldFrontIndex = frontIndex;
				frontIndex = newCapacity - 1;
				for (int j = capacity() - 1; j >= oldFrontIndex; --j, --frontIndex)
					newBlock[frontIndex] = block[j];
				++frontIndex;
			}
			block = newBlock; // Set new block to current block
		}
	}

	/**
	 * Return an element in the specific index, throw if out of range.
	 * 
	 * @param index Position in deque
	 * @return An element in this position
	 */
	private T at(int index) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException(index);
		return block[(frontIndex + index) % capacity()];
	}

	/**
	 * Override next iterator in deque
	 */
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private int index = 0;

			private void throwIfOutOfRange() {
				if (index + 1 > size)
					throw new IndexOutOfBoundsException();
			}

			/**
			 * Override hasNext method Used to check if the current pointer can go to the
			 * next element
			 * 
			 * @return true if it can go to the next element, otherwise return false
			 */
			@Override
			public boolean hasNext() {
				return index < size;
			}

			/**
			 * Override next method Used to iterate over the next object of the iterator
			 * Throws an error if the iteration is end position or the iterator is invalid
			 * 
			 * @return An element in the current pointer
			 */
			@Override
			public T next() {
				throwIfOutOfRange();
				return at(index++);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	/**
	 * Decrease iterator in deque
	 */
	public Iterator<T> descendingIterator() {
		return new Iterator<T>() {
			private int index = size - 1;

			private void throwIfOutOfRange() {
				if (index - 1 < -1)
					throw new IndexOutOfBoundsException();
			}

			/**
			 * Override hasNext method Used to check if the current pointer can go to the
			 * previous element
			 * 
			 * @return true if it can go to the previous element, otherwise return false
			 */
			@Override
			public boolean hasNext() {
				return index > -1;
			}

			/**
			 * Override next method Used to iterate over the previous object of the iterator
			 * Throws an error if the iteration is begin position or the iterator is invalid
			 * 
			 * @return An element in the current pointer
			 */
			@Override
			public T next() {
				throwIfOutOfRange();
				return at(index--);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	/**
	 * Override toString method to return the list object string in the deque
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (size > 0) {
			int curIndex = frontIndex;
			for (int i = 0; i < size - 1; ++i) {
				sb.append(block[curIndex++] + ", ");
				if (curIndex == capacity())
					curIndex = 0;
			}
			sb.append(block[curIndex]);
		}
		sb.append("]");
		LinkedList<Integer> ls;
		return sb.toString();

	}
}
