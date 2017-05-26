package com.nosliw.data.core.runtime.js;

import com.nosliw.common.utils.HAPFileUtility;

/**
 * Store information related with script  
 */
public class HAPJSScriptInfo {

	//name for the script, it is very useful when work with rhino, so that you can locate your code quickly during debuging
	private String m_name;
	
	//full file name if the script is in file
	private String m_file;
	
	//script
	private String m_script;
	
	public String isFile(){
		return this.m_file;
	}
	
	public String getName(){
		return this.m_name;
	}
	
	public String getScript(){
		if(this.m_file!=null){
			this.m_script = HAPFileUtility.readFile(m_file);
		}
		return this.m_script;
	}
	
	public static HAPJSScriptInfo buildByFile(String fileName, String name){
		HAPJSScriptInfo out = new HAPJSScriptInfo();
		out.m_file = fileName;
		out.m_name = name;
		return out;
	}

	public static HAPJSScriptInfo buildByScript(String script, String name){
		HAPJSScriptInfo out = new HAPJSScriptInfo();
		out.m_script = script;
		out.m_name = name;
		return out;
	}
}
