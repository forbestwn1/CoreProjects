package com.nosliw.core.application.common.scriptexpression;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.container.HAPItemWrapper;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPItemInContainerScriptExpression extends HAPItemWrapper{

	@HAPAttribute
	public static String SCRIPTEXPRESSION = "scriptExpression";

	public HAPItemInContainerScriptExpression() {}
	
	public HAPItemInContainerScriptExpression(HAPExpressionScript scriptExpression) {
		super(scriptExpression);
	}
	
	public HAPExpressionScript getScriptExpression() {    return (HAPExpressionScript)this.getValue();     }
	public void setScriptExpression(HAPExpressionScript expression) {    this.setValue(expression);;      }
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJSJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SCRIPTEXPRESSION, this.getScriptExpression().toStringValue(HAPSerializationFormat.JAVASCRIPT));
	}
}
