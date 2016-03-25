
public class Cookies {
	
    private int quantity;
	double cost;
	double price;
	
	public String toString() {

		return ("Quantity: " + quantity + " Cost: " + cost );
	}
	
	public Cookies(int q, double c) {
		
		quantity = q;
		cost = c; 
		price = c * 1.4; 
		
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	void setQuantity(int x) {
		quantity = x;
	}
	
	public double getPrice(){
		return price;
	}
	
	public double getCost() {
		return cost;
	}
	

}
