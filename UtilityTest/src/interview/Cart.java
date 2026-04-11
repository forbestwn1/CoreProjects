package interview;

import java.util.ArrayList;
import java.util.List;

public class Cart {

	private static int cartNumbers = 0;
	
	private List<CartItem> items;
	
	public Cart() {
		this.items = new ArrayList<CartItem>();
		synchronized(this){
			Cart.cartNumbers++;
		}
	}
	
	public List<CartItem> getItems(){
		return new ArrayList(this.items);
	}
	
}
