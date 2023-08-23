package com.nosliw.data.core.runtime.js.imp.browser;

public class HAPRuntimeBrowserUtility {

	public static String getBrowserScriptPath(String fileName){
		String keyword = "WebContent";
		return fileName.substring(fileName.indexOf(keyword)+keyword.length()+1);
	}
	
}
