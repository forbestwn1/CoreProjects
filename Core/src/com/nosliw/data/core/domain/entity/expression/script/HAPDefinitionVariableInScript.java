package com.nosliw.data.core.domain.entity.expression.script;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPDefinitionVariableInScript extends HAPSerializableImp{

	@HAPAttribute
	public static String VARIABLENAME = "variableName";
	
	private String m_variableName; 
	
	public HAPDefinitionVariableInScript(String name){
		this.m_variableName = name;
	}
	
	public String getVariableName(){
		return this.m_variableName;
	}

	public HAPDefinitionVariableInScript cloneVariableInScript() {
		return new HAPDefinitionVariableInScript(this.m_variableName);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VARIABLENAME, this.m_variableName);
	}
}
