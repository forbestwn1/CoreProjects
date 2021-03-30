package com.nosliw.data.core.script.context;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPContextDefinitionLeafConstantReference extends HAPContextDefinitionElement{

	@HAPAttribute
	public static final String CONSTANT = "constant";

	private String m_constantName;
	
	public HAPContextDefinitionLeafConstantReference() {}
	
	public HAPContextDefinitionLeafConstantReference(String constantName) {
		this.m_constantName = constantName;
	}

	@Override
	public String getType() {  return HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANTREF;	}

	public String getConstantName() {   return this.m_constantName;   }
	public void setConstantName(String name) {   this.m_constantName = name;   }
	
	@Override
	public HAPContextDefinitionElement cloneContextDefinitionElement() {
		return new HAPContextDefinitionLeafConstantReference(this.m_constantName);
	}

	@Override
	public HAPContextDefinitionElement toSolidContextDefinitionElement(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) {  return this;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONSTANT, this.getConstantName());
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPContextDefinitionLeafConstantReference) {
			HAPContextDefinitionLeafConstantReference ele = (HAPContextDefinitionLeafConstantReference)obj;
			if(!HAPBasicUtility.isEquals(this.getConstantName(), ele.getConstantName()))  return false;
			out = true;
		}
		return out;
	}	
}
