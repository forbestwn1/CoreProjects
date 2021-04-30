package com.nosliw.data.core.structure;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPElementLeafConstantReference extends HAPElement{

	@HAPAttribute
	public static final String CONSTANT = "constant";

	private String m_constantName;
	
	public HAPElementLeafConstantReference() {}
	
	public HAPElementLeafConstantReference(String constantName) {
		this.m_constantName = constantName;
	}

	@Override
	public String getType() {  return HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANTREF;	}

	public String getConstantName() {   return this.m_constantName;   }
	public void setConstantName(String name) {   this.m_constantName = name;   }
	
	@Override
	public HAPElement cloneStructureElement() {
		return new HAPElementLeafConstantReference(this.m_constantName);
	}

	@Override
	public HAPElement toSolidStructureElement(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) {  return this;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONSTANT, this.getConstantName());
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPElementLeafConstantReference) {
			HAPElementLeafConstantReference ele = (HAPElementLeafConstantReference)obj;
			if(!HAPBasicUtility.isEquals(this.getConstantName(), ele.getConstantName()))  return false;
			out = true;
		}
		return out;
	}	
}
