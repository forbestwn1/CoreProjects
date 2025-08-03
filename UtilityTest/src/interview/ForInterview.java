package interview;

public class ForInterview {

	public static void main(String[] args) {

		int out = method1(2);
		System.out.println(out);
		
	}
	
	public static int method1 (int kk) {
		try {
			if(kk==1) {
				return 1;
			} else if(kk==2) {
				throw new Exception();
			}
		}
		catch(Exception e) {
			return 2;
		}
		finally {
			return 3;
		}
	}

}

