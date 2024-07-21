package com.nosliw.core.application.division.manual.common.scriptexpression;

import java.util.Map;

import com.nosliw.common.container.HAPItemWrapper;

public class HAPManualDefinitionScriptExpressionItemInContainer extends HAPItemWrapper{

	private static String SCRIPTEXPRESSION = "expression";
	
	public HAPManualDefinitionScriptExpressionItemInContainer(String scriptExpression) {
		super(scriptExpression);
	}
	
	public String getScriptExpression() {    return (String)this.getValue();     }
	public void setScriptExpression(String scriptExpression) {    this.setValue(scriptExpression);  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SCRIPTEXPRESSION, this.getScriptExpression());
	}

}
