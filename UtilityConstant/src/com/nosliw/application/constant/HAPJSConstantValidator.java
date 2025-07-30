package com.nosliw.application.constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.utils.HAPUtilityFile;

public class HAPJSConstantValidator {

	private static String PATH_APPUI = "C:/Mywork/CoreProjects/AppUI/app/www/js";
	private static String PATH_JS = "C:/MyWork/CoreProjects/Application/WebContent/libresources/nosliw";
	private static String PATH_JS_CONSTANT = PATH_JS+"/constant";
	private static String FILE_CONSTANTS = PATH_JS_CONSTANT+"/constant.js";
	private static String FILE_COMMONCONSTANTS = PATH_JS_CONSTANT+"/COMMONCONSTANT.js";
	private static String FILE_COMMONATRIBUTECONSTANT = PATH_JS_CONSTANT+"/COMMONATRIBUTECONSTANT.js";
	
	private static Map<String, String> m_constants = new LinkedHashMap<String, String>();
	private static Map<String, String> m_commonConstants = new LinkedHashMap<String, String>();
	private static Map<String, String> m_commonAttribute = new LinkedHashMap<String, String>();
	
	public static void main(String[] args) {
		readConstantValues(FILE_CONSTANTS, m_constants);
		readConstantValues(FILE_COMMONCONSTANTS, m_commonConstants);
		readConstantValues(FILE_COMMONATRIBUTECONSTANT, m_commonAttribute);
		
		processByDir(PATH_JS);
		processByDir(PATH_APPUI);
	}
	
	private static void processByDir(String dir) {
		List<File> files = (List<File>) FileUtils.listFiles(new File(dir), new String[] {"js"}, true);
		for(File file : files) {
			try {
				FileReader inputFile = new FileReader(file);
			    BufferedReader bufferReader = new BufferedReader(inputFile);
			    int i = 0;
	            String line;
	            while ((line=bufferReader.readLine())!=null){
	            	i++;
	            	processJsLine(file, i, line, "node_CONSTANT", m_constants);
	            	processJsLine(file, i, line, "node_COMMONCONSTANT", m_commonConstants);
	            	processJsLine(file, i, line, "node_COMMONATRIBUTECONSTANT", m_commonAttribute);
	            }
	            bufferReader.close(); 			
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static void readConstantValues(String file, Map<String, String> output) {
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("buildConstants", HAPUtilityFile.readFile(file));
		InputStream javaTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPJSConstantValidator.class, "ConstantJsValidate.temp");
		String content = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
        
		Context cx = Context.enter();
        Scriptable scope = cx.initStandardObjects(null);
        NativeObject constantsObjJS = (NativeObject)cx.evaluateString(scope, content, "", 1, null);
        for(Object constantKeyObj : constantsObjJS.keySet()){
        	String constantKey = (String)constantKeyObj;
        	String value = constantsObjJS.get(constantKey)+"";
//        	System.out.println(file + "    " + constantKey + "   " + value);
        	output.put(constantKey, value);
        }
	}
	
	private static void processJsLine(File file, int lineNumber, String line, String constantType, Map<String, String> constantMap) {
		int index = 0;
		while((index=line.indexOf(constantType, index))!=-1) {
			String constantName = "";
			int end = index + constantType.length();
			String seperator = line.substring(end, end+1);
			if(".".equals(seperator)) {
				int i = end + 1;
				while(isValid(line.charAt(i))){
					constantName = constantName + line.charAt(i);
					i++;
				}
			}
			else if("[".equals(seperator)) {
				constantName = line.substring(end+2, line.indexOf("\"]", end));
			}
			if(!constantName.equals("")) {
				if(constantMap.get(constantName)==null) {
					System.out.println(file.getAbsolutePath() + "   " + lineNumber + "     " + constantType + "   " + constantName);
				}
			}
			index++;
		}
	}
	
	private static boolean isValid(char c) {
		if( (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c=='_' || c=='.') {
			return true;
		}
		return false;
	}
	
}
