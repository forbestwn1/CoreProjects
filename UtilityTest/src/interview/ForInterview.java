package interview;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ForInterview {

	public static void main(String[] args) {
        List<Integer> all = new ArrayList<Integer>();
        all.add(5);
        all.add(25);
        all.add(-234);
        all.add(342);
        all.add(-2);
        
        List<Integer> result = all.stream().filter(data->{
        	String str = data.toString();
        	return str.startsWith("2")||str.startsWith("-2");
        }).collect(Collectors.toList());
        
		System.out.println(result);
	
	}
	

}
