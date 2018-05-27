package com.nosliw.uiresource.page;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.uiresource.expression.HAPEmbededScriptExpression;

public class HAPEmbededScriptExpressionInAttribute extends HAPEmbededScriptExpression{

	@HAPAttribute
	public static final String ATTRIBUTE = "attribute";

	//attribute name
	private String m_attribute;

	public HAPEmbededScriptExpressionInAttribute(String attr, String uiId, List<Object> elements, HAPExpressionSuiteManager expressionManager){
		super(uiId, elements, expressionManager);
		this.m_attribute = attr;
	}

	public HAPEmbededScriptExpressionInAttribute(String attr, String uiId, String content, HAPExpressionSuiteManager expressionManager){
		super(uiId, content, expressionManager);
		this.m_attribute = attr;
	}
	
	public void setAttribute(String attr){this.m_attribute=attr;}
	public String getAttribute(){return this.m_attribute;}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ATTRIBUTE, this.m_attribute);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ATTRIBUTE, this.m_attribute);
	}
}
