import java.math.BigDecimal;

// ******************PUBLIC OPERATIONS*********************
// void push(x)          --> Insert x
// void pop()            --> Remove most recently inserted item
// AnyType top()         --> Return most recently inserted item
// AnyType topAndPop()   --> Return and remove most recent item
// boolean isEmpty()     --> Return true if empty; else false
// void makeEmpty()      --> Remove all items

public class Inventory {
	
	private Cookies[] theArray;
	private int topOfStack;
	private static final int DEFAULT_CAPACITY = 100;
	private static int totalStock;
	private static double highestRate;
	private static double curPrice;
	private static double totalCost;
	private static double totalRev = 0;
	
	public double getHigh() { //return current price to charge customers
		return curPrice;
	}
	
    public Inventory() { //initialize inventory
        theArray = new Cookies[DEFAULT_CAPACITY];
        topOfStack = -1;
    }
    
    
    /**
     * 
     * Method to store incoming stock in to our stack
     * With updates to total stock, selling rate and a console display of the transaction
     *
     */
    
	public void push(Cookies x) {
		totalStock += x.getQuantity();
		setTotalCost(getTotalCost() + x.getQuantity()*x.cost);
		highestRate = x.cost;
		curPrice = highestRate * 1.4;
		
		System.out.println("\n-----------BUYING-----------");
		System.out.println(x.getQuantity() + " @ $" + x.cost);
		System.out.println("Total Stock: " + totalStock + "\nTotal Cost: " + getTotalCost());

        if (topOfStack + 1 == theArray.length)
            doubleArray();
        
		theArray[++topOfStack] = x;
    }
    
    public void pop() { //pop off the top of our stack
        if (isEmpty())
            throw new IllegalArgumentException("ArrayStack pop");
        topOfStack--;
    }
    
    public Cookies top () { //return the top of our stack
        if (isEmpty())
            throw new IllegalArgumentException( "ArrayStack top" );
        return theArray[topOfStack];
    }
    
public void setTop(int x) { //directly modify the top of our stack for special cases
	
		top().setQuantity(x);
	
	}
	
    
    public Cookies topAndPop() {
        if (isEmpty()) throw new IllegalArgumentException("ArrayStack topAndPop");
        return theArray[topOfStack--];
    }
    
    public boolean isEmpty() { //checks if the stack is empty
        return topOfStack == -1;
    }
    
    public void makeEmpty() {
        topOfStack = -1;
    }
    
    private void doubleArray() { //doubles the array if we run out of space for our stack
    	Cookies[] newArray = new Cookies[theArray.length * 2];
        
        for (int i = 0; i < theArray.length; i++)
            newArray[i] = theArray[i];
        theArray = newArray;
    }
    
    /**
     * 
     * Method to sell from our stack
     * With updates to total stock, selling rate and a console display of the transaction
     *
     */

	public static void sellFront(int q, Inventory Sale) { // q is quantity requested
		System.out.println("\n-----------------SELLING------------------");

		double value = 0;
		double soldPrice = Sale.curPrice * q;
		setTotalRev(getTotalRev() + (q*Sale.curPrice));

		if (Sale.totalStock >= q) { //CONDITION: we have at least enough total stock to fulfill requested sale

			System.out.println("Sold Quantity: " + q + " Units @ $" + truncate(curPrice,2) + " each");
			Sale.totalStock = Sale.totalStock - q;
			System.out.print("Current Stock: " + Sale.totalStock + " Units");
			

			while (Sale.top().getQuantity() < q && Sale.isEmpty() == false) {
				// while current topOfStack is less than amount requested & is not empty

				value += Sale.top().getQuantity() * Sale.top().getCost(); // top quantity and its cost
				q -= Sale.top().getQuantity(); // Remaining quantity after moving on to the next shipment in my stack
				Sale.pop(); // now we pop the top of the stack since we need to take all items from that level.

			}


			if (Sale.top().getQuantity() > q) { // if current topOfStack can cover all the amount requested

				value += q * Sale.top().getCost();
				Sale.setTop(Sale.top().getQuantity() - q);// amount left in stack has changed, set new amount
				System.out.println();

			}

			else if (Sale.top().getQuantity() == q) { // if current topOfStack is exactly equal to amount requested, just pop this level

				value += q*Sale.top().getCost();
				Sale.pop(); // else (quantity requested = quantity at topOfStack, pop that top)

			}
			
			System.out.println("Transaction Sale: $" + truncate(soldPrice,2));
			System.out.println("Transaction Cost: $" + truncate(value,2));
			
		}
		

		} //

	 /**
     * 
     * Method to sell from our backorder
     * With updates to total stock, selling rate and a console display of the transaction
     *
     */
	
static void sellBack(int front, Inventory i, Backorders b) {
		
		i.setTotalRev(i.getTotalRev() + (b.getFront().cost * b.getFront().getQuantity())); 
		
		System.out.println("\n-----------------SELLING BACKORDER------------------");

		double costPrice = 0;
		double soldPrice = i.curPrice * b.getFront().getQuantity();
		//i.totalRev += (b.getFront() * i.curPrice);

		if (i.totalStock >= b.getFront().getQuantity()) {

			System.out.println("Sold Quantity: " + b.getFront().getQuantity() + " Units @ $" + truncate(i.curPrice,2) + " each");
			i.totalStock = i.totalStock - b.getFront().getQuantity();
			System.out.println("Current Stock: " + i.totalStock + " Units");

		 //	i.totalRev += (b.getFront() * i.curPrice);

			
			while (i.top().getQuantity() < b.getFront().getQuantity() && i.isEmpty() == false) {

				costPrice += i.top().getQuantity() * i.top().getCost(); // top quantity and its cost
				b.setFront(b.getFront().getQuantity() - i.top().getQuantity()); // Remaining quantity after moving on to the next shipment in my stack
				i.pop(); // now we pop the top of the stack since we need to take all items from that level.

			}

			if (i.top().getQuantity() > b.getFront().getQuantity()) { // if current topOfStack can cover all the amount requested

				costPrice += b.getFront().getQuantity() * i.top().getCost();
				i.setTop(i.top().getQuantity() - b.getFront().getQuantity());// amount left in stack has changed, set new amount
				b.dequeue();

			}

			else if (i.top().getQuantity() == b.getFront().getQuantity()) { // if current topOfStack is exactly equal to amount requested, just pop this level

				costPrice += b.getFront().getQuantity() * i.top().getCost();
				i.pop(); // else (quantity requested = quantity at topOfStack, pop that top)
				b.dequeue();

			}

			System.out.println("Transaction Sale: $" + truncate(soldPrice, 2));
			System.out.println("Transaction Cost: $" + truncate(costPrice, 2));

			if (b.isEmpty() == true) {
				System.out.println("\nAll existing back orders fulfilled.");
			}

		}

	}
	
	private static BigDecimal truncate(final double x, final int numberofDecimals) { //truncate doubles to 2 decimal places
	    return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_UP);
	}

	public int getStock() {
		return totalStock;
	}

	public static double getTotalRev() {
		return totalRev;
	}

	public static void setTotalRev(double totalRev) {
		Inventory.totalRev = totalRev;
	}

	public static double getTotalCost() {
		return totalCost;
	}

	public static void setTotalCost(double totalCost) {
		Inventory.totalCost = totalCost;
	}
}