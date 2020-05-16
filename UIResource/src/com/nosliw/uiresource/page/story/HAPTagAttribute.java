package com.nosliw.uiresource.page.story;

public class HAPTagAttribute {

	private String m_name;
	
	private String m_value;

	public HAPTagAttribute(String name, String value) {
		this.m_name = name;
		this.m_value = value;
	}
	
	public String getName() {   return this.m_name;    }
	
	public String getValue() {   return this.m_value;    }
	
}
