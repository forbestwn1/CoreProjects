package com.nosliw.common.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.json.JSONArray;

public class HAPSystemUtility {
	
	static private Properties prop;
	
	static {
		InputStream input;
		try {
			input = new FileInputStream("C:/nosliw_configure/configure.properties");
	        prop = new Properties();
	        prop.load(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String buildNosliwFullName(String name){
		return HAPConstant.NOSLIW_NAME_PREFIX+name;
	}
	
	public static String getNosliwCoreName(String name){
		int index = name.indexOf(HAPConstant.NOSLIW_NAME_PREFIX);
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

	public static String getApplicationResourceLibFolder() {	return prop.getProperty("ApplicationResourceLibFolder");	}
	public static String getApplicationResourceDataFolder() {   return prop.getProperty("ApplicationResourceDataFolder");  }
	public static String getTempFolder() {  return prop.getProperty("TempFolder");    }
	public static String getJSFolder() {   return prop.getProperty("JSFolder");  }
	public static String getAppDataFolder() {   return prop.getProperty("AppDataFolder");   }
	public static boolean getResourceCached() {   return Boolean.valueOf(prop.getProperty("ResourceCached", "false"));   }
	public static String getJSTempFolder() {   return prop.getProperty("JSTempFolder");   }
	//
	public static boolean getConsolidateLib() {   return Boolean.valueOf(prop.getProperty("ConsolidateLib", "false"));   }
	public static int getItemsPerLoop() {   return Integer.valueOf(prop.getProperty("ItemsPerLoop", "5"));   }
	//resources type that load as file
	public static Set<String> getLoadResoureByFile() {
		Set<String> out = new HashSet<String>();
		JSONArray jsonArray = new JSONArray(prop.getProperty("LoadResourceByFile", "[]"));
		for(int i=0; i<jsonArray.length(); i++) {
			out.add(jsonArray.getString(i));
		}
		return out;
	}
}
