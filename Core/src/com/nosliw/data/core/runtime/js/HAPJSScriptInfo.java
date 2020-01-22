package com.nosliw.data.core.runtime.js;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPFileUtility;

/**
 * Store information related with script  
 */
@HAPEntityWithAttribute
public class HAPJSScriptInfo extends HAPSerializableImp{

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String FILE = "file";

	@HAPAttribute
	public static String SCRIPT = "script";

	//name for the script, it is very useful when work with rhino, so that you can locate your code quickly during debuging
	private String m_name;
	
	//full file name if the script is in file
	private String m_file;
	
	//script
	private StringBuffer m_script;

	//type of resource for this script(lib, data type, ...)
	private String m_type;
	
	public String isFile(){	return this.m_file;	}
	public void setFile(String file){	this.m_file = file;	}
	
	public String getType() {   return this.m_type;   }
	public void setType(String type) {   this.m_type = type;    }
	
	public String getName(){	return this.m_name;	}
	
	public String getScript(){
		if(this.m_file!=null){
			this.m_script = new StringBuffer().append(HAPFileUtility.readFile(m_file));
		}
		return this.m_script.toString();
	}
	
	public void setScript(String script){ 
		if(script==null)  this.m_script = null;
		else {
			this.m_script = new StringBuffer();
			this.m_script.append(script);
		}
	}
	
	public void appendScript(String script){		this.m_script.append(script);	}
	
	public static HAPJSScriptInfo buildByFile(String fileName, String name){
		HAPJSScriptInfo out = new HAPJSScriptInfo();
		out.m_file = fileName;
		out.m_name = name;
		return out;
	}

	public static HAPJSScriptInfo buildByScript(String script, String name){
		HAPJSScriptInfo out = new HAPJSScriptInfo();
		out.m_script = new StringBuffer().append(script);
		out.m_name = name;
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(NAME, this.m_name);
		
		if(this.m_script!=null)		jsonMap.put(SCRIPT, this.m_script.toString());
		jsonMap.put(FILE, this.m_file);
	}


}
