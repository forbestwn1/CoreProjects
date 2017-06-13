package com.nosliw.data.core.runtime.js.rhino;

import java.io.File;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.nosliw.common.utils.HAPFileUtility;

public class HAPRhinoRuntimeUtility {

	private static int index = 1;
	
	private static String scriptTempFolder = "C:/Temp/ScriptExport/scripts/" + System.currentTimeMillis() + "/";

	
	public static void loadScript(String script, Context context, Scriptable scope, String name){
		try{
			String folder = getScriptTempFolder();
			String scriptTempFile = folder + "/" + String.format("%03d", index++) + "_" + name+".js";
			HAPFileUtility.writeFile(scriptTempFile, script);
			
			context.evaluateString(scope, script, name, 1, null);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static String getScriptTempFolder(){
		File directory = new File(scriptTempFolder);
	    if (! directory.exists()){
	    	directory.mkdir();
	    }
	    return directory.getAbsolutePath();
	}
}
