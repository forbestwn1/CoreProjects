package com.nosliw.data.core.script.expression1.imp.expression;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.updatename.HAPUpdateName;

public class HAPVariableInScript extends HAPSerializableImp{

	@HAPAttribute
	public static String VARIABLENAME = "variableName";
	
	private String m_variableName; 
	
	public HAPVariableInScript(String name){
		this.m_variableName = name;
	}
	
	public String getVariableName(){
		return this.m_variableName;
	}

	public String updateName(HAPUpdateName nameUpdate) {
		this.m_variableName = nameUpdate.getUpdatedName(m_variableName);
		return this.m_variableName;
	}

	public HAPVariableInScript cloneVariableInScript() {
		return new HAPVariableInScript(this.m_variableName);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VARIABLENAME, this.m_variableName);
	}
}
