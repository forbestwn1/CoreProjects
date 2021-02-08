package com.nosliw.common.utils;

public class HAPNosliwUtility {

	public static String buildNosliwFullName(String name){
		return HAPConstantShared.NOSLIW_NAME_PREFIX+name;
	}
	
	public static String getNosliwCoreName(String name){
		int index = name.indexOf(HAPConstantShared.NOSLIW_NAME_PREFIX);
		if(index==-1)  return null;
		return name.substring(index);
	}
	
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
