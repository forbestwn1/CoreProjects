package com.nosliw.core.application.common.scriptexpression;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPItemInContainerScriptExpression extends HAPEntityInfoImp{

	@HAPAttribute
	public static String SCRIPTEXPRESSION = "scriptExpression";

	private HAPExpressionScript m_scriptExpression;
	
	public HAPItemInContainerScriptExpression() {}
	
	public HAPItemInContainerScriptExpression(HAPExpressionScript scriptExpression) {
		this.m_scriptExpression = scriptExpression;
	}
	
	public HAPExpressionScript getScriptExpression() {    return this.m_scriptExpression;     }
	public void setScriptExpression(HAPExpressionScript expression) {    this.m_scriptExpression = expression;      }
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SCRIPTEXPRESSION, this.m_scriptExpression.toStringValue(HAPSerializationFormat.JAVASCRIPT));
	}
}
