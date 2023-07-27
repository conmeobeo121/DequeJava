import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 
 * @author ADMIN
 * Implement deque (Double-ended queue) in Java
 * @param <T>
 */
@SuppressWarnings("unchecked")
public class Deque<T> implements Iterable<T> {
	private T[] blocks; // Block data
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
	 * Construct deque with specific capacity
	 * This capacity must be positive (or equal to 0)
	 * @param capacity The specific capacity when call constructor.
	 */
	public Deque(int capacity) {
		if (capacity < 0)
			throw new IllegalArgumentException("Illegal capacity");
		this.frontIndex = this.backIndex = this.size = 0;
		this.blocks = (T[]) new Object[capacity];
	}

	/**
	 * Clear all element in deque
	 * This method just clear data, no resize
	 */
	public void clear() {
		for (int i = 0; i < capacity(); ++i)
			this.blocks[i] = null;
		this.frontIndex = this.backIndex = this.size = 0;
	}

	/**
	 * Return the size of deque
	 * @return Size (or number of element) in deque.
	 */
	public int size() {
		return this.size;
	}

	/**
	 * Return the capacity of deque
	 * @return Capacity (the max number of memory block) in deque.
	 */
	public int capacity() {
		return this.blocks.length;
	}

	/**
	 * Add object to deque
	 * This method will add object to last position in the deque,
	 * When the deque is full (i.e the size equal to the capacity), it will
	 * return false to notify the deque is full and you can't add any object
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
	 * Add object to deque
	 * This method will add element to last position in the deque,
	 * When the deque is full (i.e the size equal to the capacity), it
	 * will auto resize the block, so you can call this method any time you 
	 * want
	 * @param object Object for add
	 */
	public void addLast(T object) {
		// Call private method add object which type is 1 to add object
		//in the last position deque
		addObject(object, 1);
	}

	/**
	 * Add object to deque
	 * This method will add object to first position in the deque,
	 * When the deque is full (i.e the size equal to the capacity), it
	 * will auto resize the block, so you can call this method any time you 
	 * want
	 * @param object Object for add
	 */
	public void addFirst(T object) {
		// Call private method add object which type is 0 to add object
		// in the first position deque
		addObject(object, 0);
	}

	/**
	 * Remove and retrieve the first object in deque
	 * This method will return null if the deque is empty
	 * @return first object is deque, null if the deque is empty
	 */
	public T popFirst() {
		// Call private method pop object which type is 0 to remove
		// and retrieve first object in deque
		return popObject(0);
	}

	/**
	 * Remove and retrieve the last object in deque
	 * This method will return null if the deque is empty
	 * @return last object is deque, null if the deque is empty
	 */
	public T popLast() {
		// Call private method pop object which type is 1 to remove
		// and retrieve last object in deque
		return popObject(1);
	}
	
	/**
	 * Retrieve the first object in deque
	 * This method will return null if the deque is empty
	 * @return first object is deque, null if the deque is empty
	 */
	public T peekFirst() {
		// Call private method peek object which type is 0 to 
		// retrieve first object in deque
		return peekObject(0);
	}

	/**
	 * Retrieve the last object in deque
	 * This method will return null if the deque is empty
	 * @return last object is deque, null if the deque is empty
	 */
	public T peekLast() {
		// Call private method peek object which type is 1 to 
		// retrieve last object in deque
		return peekObject(1);
	}

	/**
	 * Private function to add new object in deque
	 * @param object Object for add
	 * @param typePush Type for add, 0: add to the first position, 1: add to the last position 
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
			blocks[frontIndex] = object;
		} else {
			blocks[backIndex++] = object;
			if (backIndex == capacity())
				backIndex = 0;
		}
		++size;
	}

	/**
	 * Private method to remove and retrieve the first or the last object in deque
	 * if the deque is empty, return null
	 * @param typePop // Type for remove, 0: remove and retrieve first object in deque, 1: remove and retrieve last object in deque
	 * @return first or last object in deque depend on typePop argument, null if the deque is empty
	 */
	private T popObject(int typePop) {
		if (typePop < 0 || typePop > 1)
			throw new IllegalArgumentException("Invalid type pop"); // Throw if type doesn't match
		T object = null;
		if (size != 0) {
			if (typePop == 0) {
				object = blocks[frontIndex];
				blocks[frontIndex++] = null;
				if (frontIndex == capacity())
					frontIndex = 0;
			} else {
				--backIndex;
				if (backIndex < 0)
					backIndex = capacity() - 1;
				object = blocks[backIndex];
				blocks[backIndex] = null;
			}
			--size;
		}
		return object;
	}

	/**
	 * Private method to retrieve the first or the last object in deque
	 * if the deque is empty, return null
	 * @param typePop // Type for retrieve, 0: retrieve first object in deque, 1: retrieve last object in deque
	 * @return first or last object in deque depend on typePeek argument, null if the deque is empty
	 */
	private T peekObject(int typePeek) {
		if (typePeek < 0 || typePeek > 1)
			throw new IllegalArgumentException("Invalid type pop");
		T object = null;
		if (size != 0)
			if (typePeek == 0)
				object = blocks[frontIndex];
			else {
				if (backIndex - 1 < 0)
					object = blocks[capacity() - 1];
				else
					object = blocks[backIndex - 1];
			}

		return object;
	}

	/**
	 * Method to resize the deque
	 * @param newCapacity new capacity to resize
	 */
	private void resizeDeque(int newCapacity) {
		// If new capacity is greater than old capacity, resize
		if (newCapacity > capacity()) {
			T[] newBlocks = (T[]) new Object[newCapacity]; // Allocate new block
			if (size > 0) {
				// Copy data if size > 0
				for (int i = 0; i < backIndex; ++i)
					newBlocks[i] = blocks[i];
				int oldFrontIndex = frontIndex;
				frontIndex = newCapacity - 1;
				for (int j = capacity() - 1; j >= oldFrontIndex; --j, --frontIndex)
					newBlocks[frontIndex] = blocks[j];
				++frontIndex;
			}
			blocks = newBlocks; // Set new block to current block
		}
	}

	/**
	 * Override iterator in deque
	 */
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			int nextIndex = frontIndex;
			int currentSize = size;
			int currentCapacity = capacity();
			/**
			 * Method use to check if iterator is valid or not
			 * Used to confirm if the iterator is valid or not if the deque user 
			 * uses methods to change the data in the deque such as add or remove
			 */
			private void validIterator() {
				// Make sure no resize when iterate in deque
				if (currentCapacity != capacity() && currentSize != size)
					throw new ConcurrentModificationException();
				if (size == 0) {
					// If size is zero then check if iterator is end or not
					if (nextIndex != frontIndex)
						throw new ConcurrentModificationException();
				} else {
					// Make sure the iterator is valid
					if ((frontIndex + size < capacity() && !(nextIndex >= frontIndex && nextIndex <= backIndex))
							|| (!(frontIndex + size < capacity())
									&& !(nextIndex >= frontIndex || nextIndex <= backIndex)))
						throw new ConcurrentModificationException();
				}
			}

			/**
			 * Override hasNext method
			 * Used to check if the iterator can continue 
			 * to loop through the next object in deque
			 */
			@Override
			public boolean hasNext() {
				validIterator();
				return nextIndex != backIndex;
			}

			/**
			 * Override hasNext method
			 * Used to iterate over the next object of the iterator
			 * Throws an error if the iteration ends or the iterator is invalid
			 */
			@Override
			public T next() {
				validIterator();
				if (nextIndex == backIndex)
					throw new IndexOutOfBoundsException("Iterator went to end");
				T oldVal = blocks[nextIndex++];
				if (nextIndex == capacity())
					nextIndex = 0;
				return oldVal;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
	
	/**
	 * Override toString method to return the list object string in 
	 * the deque
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (size > 0) {
			int curIndex = frontIndex;
			for (int i = 0; i < size - 1; ++i) {
				sb.append(blocks[curIndex++] + ", ");
				if (curIndex == capacity())
					curIndex = 0;
			}
			sb.append(blocks[curIndex]);
		}
		sb.append("]");
		return sb.toString();
	}
	
	public static void printInformationDeque(Deque<?> deque) {
		System.out.println(deque.size());
		System.out.println(deque.capacity());
		System.out.println(deque);
	}

	public static void main(String[] args) {
		Deque<String> dq = new Deque<>(2);
		dq.add("Hello World");
		dq.addFirst("World Hello");
		dq.addLast("Hello World Hello");
		dq.addFirst("World Hello World");
		dq.addFirst("World Hello Hello World");
		dq.addLast("Hello World Hello World");
		printInformationDeque(dq);
		dq = new Deque<>(0);
		dq.addFirst("Hello World");
		dq.addFirst("World Hello");
		dq.addFirst("World Hello World");
		dq.addFirst("World Hello Hello World");
		dq.add("World Hello Hello World World");
		dq.add("Hello Hello");
		printInformationDeque(dq);
		dq.addLast("World Hello Hello World World");
		dq.addLast("Hello Hello");
		printInformationDeque(dq);
		dq = new Deque<>(0);
		dq.addLast("Hello World");
		dq.addLast("World Hello");
		dq.addLast("World Hello World");
		dq.addLast("World Hello Hello World");
		dq.addLast("Hello World");
		dq.addLast("World Hello");
		dq.addLast("World World Hello");
		dq.addLast("World Hello Hello");
		dq.add("HehhEllo");
		dq.add("HehhEllo Olfle");
		dq.addLast("HehhEllo Olfle");
		printInformationDeque(dq);
		dq.clear();
		dq.add("1234");
		dq.add("4321");
		dq.addFirst("4321123");
		dq.add("29349");
		dq.addFirst("59494");
		Iterator<String> iter = dq.iterator();
		while (iter.hasNext()) {
			String txt = iter.next();
			System.out.println(txt);
		}
	}
}
