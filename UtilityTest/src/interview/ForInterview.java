package interview;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class ForInterview {

	
	public static void main(String[] args) {

		List<String> names = Arrays.asList("Jake", "Sophia", "Lucas", "Mia", "Benjamin");
		Map<Integer, List<String>> groupedByLength = names.stream()
		    .collect(Collectors.groupingBy(name->name.length()));

		System.out.println(groupedByLength);
	
		ExecutorService executor = Executors.newFixedThreadPool(5);
		Future<Integer> future = executor.submit(()->{
			
			throw new Exception("hello error!!!!");
		});
        try {
			Integer out = future.get();
			System.out.println(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
        executor.shutdown();
	}
	

}
