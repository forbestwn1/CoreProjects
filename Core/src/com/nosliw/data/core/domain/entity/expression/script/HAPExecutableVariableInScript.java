package com.nosliw.data.core.domain.entity.expression.script;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.runtime.HAPExecutableImp;

public class HAPExecutableVariableInScript extends HAPExecutableImp{

	@HAPAttribute
	public static String VARIABLENAME = "variableName";
	
	@HAPAttribute
	public static String VARIABLEKEY = "variableKey";
	
	private String m_variableName; 
	
	private String m_variableKey;
	
	public HAPExecutableVariableInScript(String name, String varKey){
		this.m_variableName = name;
		this.m_variableKey = varKey;
	}
	
	public String getVariableName(){	return this.m_variableName;	}

	public String getVariableKey() {    return this.m_variableKey;     }
	
	public HAPExecutableVariableInScript cloneVariableInScript() {
		return new HAPExecutableVariableInScript(this.m_variableName, this.m_variableKey);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VARIABLENAME, this.m_variableName);
		jsonMap.put(VARIABLEKEY, this.m_variableKey);
	}
}
