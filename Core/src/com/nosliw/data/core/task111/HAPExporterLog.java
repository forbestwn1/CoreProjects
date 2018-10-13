package com.nosliw.data.core.task111;

import java.io.File;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPExporterLog {

	private static String logTempFolder = HAPFileUtility.getTaskLogFolder() + System.currentTimeMillis() + "/";

	public static void exportLog(String name, HAPLog log) {
		
		String scriptTempFile = getScriptTempFolder() + "/" + name+".log";
		HAPFileUtility.writeFile(scriptTempFile, HAPJsonUtility.formatJson(log.toStringValue(HAPSerializationFormat.JSON)));
	}

	private static String getScriptTempFolder(){
		File directory = new File(logTempFolder);
	    if (! directory.exists()){
	    	directory.mkdir();
	    }
	    return directory.getAbsolutePath();
	}

}
