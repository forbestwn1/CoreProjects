package com.nosliw.common.utils;

public class HAPSystemUtility {
	
	public static String getHAPBaseClassName(Class cs){
		String out = null;
		String name = cs.getSimpleName();
		if(name.startsWith("HAP")){
			out = name.substring("HAP".length());
		}
		return out;
	}

	public static boolean isHAPClass(Class cs){
		return cs.getSimpleName().startsWith("HAP");
	}
	
	public static boolean isHAPClass(String csName){
		int index = csName.lastIndexOf(".");
		String className = csName.substring(index+1);
		return className.startsWith("HAP");
	}
}
