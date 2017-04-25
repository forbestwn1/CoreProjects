package com.nosliw.data.core.runtime.js.rhino;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValue;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSScriptUtility;

public class ScriptTracker {

	private List<String> m_scripts;
	
	private List<String> m_files;
	
	private String exportPath = "c:/temp/scriptexport/";
	
	ScriptTracker(){
		this.m_scripts = new ArrayList<String>();
		this.m_files = new ArrayList<String>();
	}
	
	public void addScript(String script){
		this.m_scripts.add(script);
	}
	
	public void addFile(String file){
		this.m_files.add(file);
	}
	
	public void export(){
		StringBuffer scriptContent = new StringBuffer();
		
		for(String file : this.m_files){
			scriptContent.append("<script src=\""+file+"\"></script>\n");
		}

		scriptContent.append("\n");
		scriptContent.append("\n");
		scriptContent.append("<script>\n");

		for(String script : this.m_scripts){
			scriptContent.append(script);
			scriptContent.append("\n");
		}
		
		scriptContent.append("\n</script>\n");
		
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("script", scriptContent.toString());
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(ScriptTracker.class, "scriptTracker.temp");
		String out = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		
		HAPFileUtility.writeFile(exportPath+"1.html", out);
	}
}
