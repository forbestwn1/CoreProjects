package com.nosliw.common.constant;

public class Main {

	public static void main(String[] args) {
		System.out.println(Main1.attr);
		System.out.println(Main2.attr);

		Main1.attr = "main3";
		System.out.println(Main1.attr);
		System.out.println(Main2.attr);
		
		Main2.attr = "main2";
		System.out.println(Main1.attr);
		System.out.println(Main2.attr);
		
		
	}

}
