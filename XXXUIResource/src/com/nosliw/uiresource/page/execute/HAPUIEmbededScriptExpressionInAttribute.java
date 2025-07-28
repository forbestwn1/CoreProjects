package com.nosliw.uiresource.page.execute;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEmbededScriptExpressionInAttribute;

public class HAPUIEmbededScriptExpressionInAttribute extends HAPUIEmbededScriptExpression{

	@HAPAttribute
	public static final String ATTRIBUTE = "attribute";

	private String m_attribute;
	
	public HAPUIEmbededScriptExpressionInAttribute(HAPDefinitionUIEmbededScriptExpressionInAttribute uiEmbededScriptExpressionAttr){
		super(uiEmbededScriptExpressionAttr);
		this.m_attribute = uiEmbededScriptExpressionAttr.getAttribute();
	}

	public String getAttribute(){return this.m_attribute; }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ATTRIBUTE, this.getAttribute());
	}
}
