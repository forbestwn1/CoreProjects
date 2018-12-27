package com.nosliw.uiresource.page.execute;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEmbededScriptExpressionInAttribute;

public class HAPUIEmbededScriptExpressionInAttribute extends HAPUIEmbededScriptExpression{

	@HAPAttribute
	public static final String ATTRIBUTE = "attribute";

	public HAPUIEmbededScriptExpressionInAttribute(HAPDefinitionUIEmbededScriptExpressionInAttribute uiEmbededScriptExpressionAttr){
		super(uiEmbededScriptExpressionAttr);
	}

	public String getAttribute(){return ((HAPDefinitionUIEmbededScriptExpressionInAttribute)this.getDefinition()).getAttribute(); }

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ATTRIBUTE, this.getAttribute());
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ATTRIBUTE, this.getAttribute());
	}
}
