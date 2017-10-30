package com.nosliw.uiresource;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.uiresource.expression.HAPScriptExpression;

public class HAPEmbededScriptExpressionInAttribute extends HAPEmbededScriptExpression{

	@HAPAttribute
	public static final String ATTRIBUTE = "attribute";

	//attribute name
	private String m_attribute;

	public HAPEmbededScriptExpressionInAttribute(String attr, String uiId, HAPScriptExpression scriptExpression){
		super(uiId, scriptExpression);
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
