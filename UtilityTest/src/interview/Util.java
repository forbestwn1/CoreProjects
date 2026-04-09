package interview;

public class Util {

	private int length;
	
	public Util(int length) {
		this.length = length;
	}
	
	public String compare(StringCompare strCompare) {
	
		int len1 = strCompare.getFirst()==null?0 : strCompare.getFirst().length();
		int len2 = strCompare.getSecond()==null?0 : strCompare.getSecond().length();
		
		if(len1+len2>this.length) {
			return null;
		} else {
			return strCompare.getSecond();
		}
	}
	
}
