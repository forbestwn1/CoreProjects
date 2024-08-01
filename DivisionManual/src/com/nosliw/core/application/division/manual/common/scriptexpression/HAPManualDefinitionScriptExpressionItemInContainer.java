package com.nosliw.core.application.division.manual.common.scriptexpression;

import java.util.Map;

import com.nosliw.common.container.HAPItemWrapper;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPManualDefinitionScriptExpressionItemInContainer extends HAPItemWrapper{

	private static String SCRIPTEXPRESSION = "expression";
	
	public HAPManualDefinitionScriptExpressionItemInContainer(HAPManualDefinitionScriptExpression scriptExpression) {
		super(scriptExpression);
	}
	
	public HAPManualDefinitionScriptExpression getScriptExpression() {    return (HAPManualDefinitionScriptExpression)this.getValue();     }
	public void setScriptExpression(HAPManualDefinitionScriptExpression scriptExpression) {    this.setValue(scriptExpression);  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SCRIPTEXPRESSION, this.getScriptExpression().toStringValue(HAPSerializationFormat.JSON));
	}

}
