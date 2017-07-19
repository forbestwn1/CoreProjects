package com.nosliw.app.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPApplicationUtility {

	/*
	 * 
	 */
	static public List<String> getFileNames(HAPConfigure filePathConfigure){
		List<String> out = new ArrayList<String>();
		
		String path = filePathConfigure.getConfigureValue("path").getStringValue();
		List<String> fileNames = filePathConfigure.getConfigureValue("files").getListValue(String.class);
		for(String fileName : fileNames){
			out.add(HAPFileUtility.buildFullFileName(path, fileName));
		}
		return out;
	}
	
	/*
	 * 
	 */
	static public List<InputStream> getFileInputStreams(HAPConfigure filePathConfigure){
		List<InputStream> out = new ArrayList<InputStream>();
		
		String path = filePathConfigure.getConfigureValue("path").getStringValue();
		List<String> fileNames = filePathConfigure.getConfigureValue("files").getListValue(String.class);
		for(String fileName : fileNames){
			out.add(HAPFileUtility.getInputStreamFromFile(path, fileName));
		}
		return out;
	}
	
}
