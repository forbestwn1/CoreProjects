package interview;

import java.util.List;

public class Util {

    public static int sumupPrice(List<Cart> carts, FiterFunction<CartItem> filter) {
    	int out = 0;
         
    	if(carts != null) {
        	for(Cart cart : carts) {
        		for(CartItem item : cart.getItems()) {
        			
        			if(filter.filter(item)) {
        				out = out + item.getPrice();
        			}
        			
//        			if(item.getPrice()%2==0) {
//        				out = out + item.getPrice();
//        			}
        			
        		}
        	}
    		
    	}
    	
    	
    	return out;
    }
	
    //2 cart each cart have 2 items
    
    //edge case
    //
    
}


public interface FiterFunction<T>{
	boolean filter(T t);
}









