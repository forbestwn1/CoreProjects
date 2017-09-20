package com.nosliw.uiresource;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;

public class HAPScriptExpressionInAttribute extends HAPScriptExpressionInContent{

	@HAPAttribute
	public static final String ATTRIBUTE = "attribute";

	//attribute name
	private String m_attribute;

	public HAPScriptExpressionInAttribute(String attr, String uiId, HAPScriptExpression scriptExpression){
		super(scriptExpression);
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
