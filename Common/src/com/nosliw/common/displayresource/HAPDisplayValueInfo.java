package com.nosliw.common.displayresource;

public class HAPDisplayValueInfo {

	private String m_name;
	private HAPDisplayResourceNode m_displayResource;
	private String m_fallBack;

	public HAPDisplayValueInfo(String name, HAPDisplayResourceNode displayResource, String fallBack) {
		this.m_name = name;
		this.m_displayResource = displayResource;
		this.m_fallBack = fallBack;
	}
	
	public String getName() {   return this.m_name;   }
	
	public HAPDisplayResourceNode getDisplayResource() {   return this.m_displayResource;     }
	
	public String getFallBackValue() {   return this.m_fallBack;    }
	
	public String getDisplayValue() {
		String labelDisplay = this.m_displayResource.getValue(this.m_name);
		if(labelDisplay==null)  labelDisplay = this.m_fallBack; 
		return labelDisplay;
	}
}
