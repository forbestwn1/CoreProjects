package com.nosliw.data.datatype.importer.js;

public class AAAA {
	public static void main(String[] args){
		System.out.print(new BBB().test());
	}
}

class BBB extends CCC{
//	public static String VALUE = "7777"; 
	
	public String test(){
		BBB.VALUE = "666";
		return BBB.VALUE + "  " + CCC.VALUE;
	}
}

class CCC{
	public static String VALUE = "5555"; 
}
