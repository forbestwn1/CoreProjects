package com.nosliw.core.application.common.scriptexpression;

import java.util.Map;

import com.nosliw.common.container.HAPItemWrapper;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPDefinitionScriptExpressionItemInContainer extends HAPItemWrapper{

	private static String SCRIPTEXPRESSION = "expression";
	
	public HAPDefinitionScriptExpressionItemInContainer(HAPDefinitionScriptExpression scriptExpression) {
		super(scriptExpression);
	}
	
	public HAPDefinitionScriptExpression getScriptExpression() {    return (HAPDefinitionScriptExpression)this.getValue();     }
	public void setScriptExpression(HAPDefinitionScriptExpression scriptExpression) {    this.setValue(scriptExpression);  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SCRIPTEXPRESSION, this.getScriptExpression().toStringValue(HAPSerializationFormat.JSON));
	}

}
