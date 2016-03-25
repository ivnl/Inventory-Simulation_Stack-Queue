
public class Backorders {

	// ******************PUBLIC OPERATIONS*********************
	// void enqueue(x) --> Insert x
	// AnyType getFront() --> Return least recently inserted item
	// AnyType dequeue() --> Return and remove least recent item
	// boolean isEmpty() --> Return true if empty; else false
	// void makeEmpty() --> Remove all items

		
		private Cookies[] theArray;
		private int currentSize = 0;
		private int front = 0;
		private int back = -1;
		private static final int DEFAULT_CAPACITY = 50;
		public static double savePrice = 0;

	public Backorders() {
			theArray = new Cookies[DEFAULT_CAPACITY];
		}

		public void enqueue(Cookies x) {
			if (currentSize == theArray.length)
				doubleQueue();
			back = ++back % theArray.length;
			theArray[back] = x;
			currentSize++;
		}

		public Cookies getFront() {
			if (isEmpty())
			throw new IndexOutOfBoundsException("ArrayQueue getFront");
			return theArray[front];
		}
		
		public void setFront(int x) {
			if (isEmpty())
			throw new IndexOutOfBoundsException("ArrayQueue getFront");
			theArray[front].setQuantity(x);
		}

		public int dequeue() {
	        if (isEmpty()) throw new IndexOutOfBoundsException( "ArrayQueue dequeue" );
	        
	        currentSize--;
		int returnValue = theArray[front].getQuantity();
	        front = ++front % theArray.length;
	        return returnValue;
	    }

		public boolean isEmpty() {
			return currentSize == 0;
		}

		public void makeEmpty() {
			currentSize = 0;
			front = 0;
			back = -1;
		}

		private void doubleQueue() {
			Cookies[] newArray = new Cookies[theArray.length * 2];

			for (int i = 0; i < currentSize; i++) {
				newArray[i] = theArray[front];
				front = front % theArray.length;
			}

			theArray = newArray;
			front = 0;
			back = currentSize - 1;
		}


}
