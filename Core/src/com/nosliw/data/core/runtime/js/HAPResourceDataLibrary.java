package com.nosliw.data.core.runtime.js;

import com.nosliw.data.core.runtime.HAPResourceData;

public class HAPResourceDataLibrary implements HAPResourceData{

	private String m_script;
	
	public HAPResourceDataLibrary(String script){
		this.m_script = script;
	}
	
	public String getScript(){
		return this.m_script;
	}
	
}
