package com.nosliw.data.core.runtime.js.rhino;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPFileUtility;

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
		StringBuffer out = new StringBuffer();
		
		for(String file : this.m_files){
			out.append("<script src=\""+file+"\"></script>");
		}
		
		for(String script : this.m_scripts){
			out.append(script);
		}
		
		HAPFileUtility.writeFile(exportPath+"1.script", out.toString());
	}
}
