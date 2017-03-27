package com.nosliw.data.core.runtime.js;

public class HAPJSLibrary {

	private String m_name;
	
	private String m_version;

	public HAPJSLibrary(String name, String version){
		this.m_name = name;
		this.m_version = version;
	}
	
	public String getName(){		return this.m_name;	}
	
	public String getVersion(){   return this.m_version;  }
	
}
