package com.nosliw.core.application.common.scriptexpression;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.runtime.HAPExecutableImp;

@HAPEntityWithAttribute
public class HAPManualVariableInScript extends HAPExecutableImp{

	@HAPAttribute
	public static String VARIABLENAME = "variableName";
	
	@HAPAttribute
	public static String VARIABLEKEY = "variableKey";
	
	private String m_variableName; 
	
	private String m_variableKey;
	
	public HAPManualVariableInScript(String name){
		this.m_variableName = name;
	}
	
	public HAPManualVariableInScript(String name, String varKey){
		this.m_variableName = name;
		this.m_variableKey = varKey;
	}
	
	public String getVariableName(){	return this.m_variableName;	}

	public String getVariableKey() {    return this.m_variableKey;     }
	public void setVariableKey(String varKey) {     this.m_variableKey = varKey;      }
	
	public HAPManualVariableInScript cloneVariableInScript() {
		return new HAPManualVariableInScript(this.m_variableName, this.m_variableKey);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VARIABLENAME, this.m_variableName);
		jsonMap.put(VARIABLEKEY, this.m_variableKey);
	}
}
