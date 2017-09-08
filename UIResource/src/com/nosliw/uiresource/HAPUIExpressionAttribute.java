package com.nosliw.uiresource;

import java.util.Map;

import com.nosliw.data.core.HAPDataTypeManager;

public class HAPUIExpressionAttribute extends HAPUIExpressionContent{
	//attribute name
	private String m_attribute;

	public HAPUIExpressionAttribute(String uiId, String attr, String content){
		super(uiId, content);
		this.m_attribute = attr;
	}
	
	public void setAttribute(String attr){this.m_attribute=attr;}
	public String getAttribute(){return this.m_attribute;}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ATTRIBUTE, this.m_attribute);
	}
}
