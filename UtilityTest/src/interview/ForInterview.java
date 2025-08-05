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


select * from Employee_Master e1 inner join Employee_Master e2 on e1.managerId = e2.empId where empId = "a"




class Resource {
	
	public static int data;
	
}

class producer {
	
	public create(int d) {
		while(true) {
			syncnized(Resource.class){
				Resource.data = d;
				notify();
			}
		}
	}
}

class consumer {

	public int consume() {
		while(true) {
			syncnized(Resource.class){
				wait();
				return Resource.data;
			}
		}
	}
	
}
