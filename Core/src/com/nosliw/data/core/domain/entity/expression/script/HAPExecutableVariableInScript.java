package com.nosliw.data.core.domain.entity.expression.script;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.data.variable.HAPIdVariable;
import com.nosliw.data.core.runtime.HAPExecutableImp;

public class HAPExecutableVariableInScript extends HAPExecutableImp{

	@HAPAttribute
	public static String VARIABLENAME = "variableName";
	
	@HAPAttribute
	public static String VARIABLEID = "variableId";
	
	private String m_variableName; 
	
	private HAPIdVariable m_variableId;
	
	public HAPExecutableVariableInScript(String name, HAPIdVariable varId){
		this.m_variableName = name;
		this.m_variableId = varId;
	}
	
	public String getVariableName(){
		return this.m_variableName;
	}

	public HAPExecutableVariableInScript cloneVariableInScript() {
		return new HAPExecutableVariableInScript(this.m_variableName, this.m_variableId);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VARIABLENAME, this.m_variableName);
		jsonMap.put(VARIABLEID, this.m_variableId.toStringValue(HAPSerializationFormat.JSON));
	}
}
