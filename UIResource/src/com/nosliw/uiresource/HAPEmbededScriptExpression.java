package com.nosliw.uiresource;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.uiresource.expression.HAPScriptExpression;

@HAPEntityWithAttribute
public class HAPEmbededScriptExpression extends HAPSerializableImp{

	@HAPAttribute
	public static final String SCRIPTEXPRESSION = "scriptExpression";
	
	private HAPScriptExpression m_scriptExpression;
	
	public HAPEmbededScriptExpression(HAPScriptExpression scriptExpression){
		this.m_scriptExpression = scriptExpression;
	}

	public HAPScriptExpression getScriptExpression(){
		return this.m_scriptExpression;
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(SCRIPTEXPRESSION, this.m_scriptExpression.toStringValue(HAPSerializationFormat.JSON));
	}
	
}
