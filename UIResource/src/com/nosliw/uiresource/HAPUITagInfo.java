package com.nosliw.uiresource;

public class HAPUITagInfo {

	private String m_name;
	
	private String m_script;
	
	public HAPUITagInfo(String name, String script){
		this.m_name = name;
		this.m_script = script;
	}
	
	public String getName(){return this.m_name;}
	
	public String getScript(){return this.m_script;}
	
}
