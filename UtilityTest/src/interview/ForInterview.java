package interview;

public class ForInterview {

	static int[] prices = {7, 1, 5, 3, 6, 4};
	
	
	public static void main(String[] args) {
		
		
		int max = 0;
		int minStartPrice = -1;
        for(int i=0; i<prices.length; i++) {
        	if(prices[i]<minStartPrice) {
        		minStartPrice = prices[i];
            	for(int j=i+1; j<prices.length; j++) {
            		int profit = prices[j] - prices[i];
            		if(profit>max) {
            			max = profit;
            		}
            	}
        	}
        }
		
		
	}
	
}
