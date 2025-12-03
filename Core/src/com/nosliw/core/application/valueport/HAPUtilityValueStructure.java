package com.nosliw.core.application.valueport;

public class HAPUtilityValueStructure {

	public static int sortPriority(double p1, double p2) {
		var d = p1 - p2;
		if(d==0) {
			return 0;
		}
		else if(d<0) {
			return -1;
		}
		else{
			return 1;
		}
	}
	
}
