package com.nosliw.data.core.structure;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPElementStructureLeafConstantReference extends HAPElementStructure{

	@HAPAttribute
	public static final String CONSTANT = "constant";

	private String m_constantName;
	
	public HAPElementStructureLeafConstantReference() {}
	
	public HAPElementStructureLeafConstantReference(String constantName) {
		this.m_constantName = constantName;
	}

	@Override
	public String getType() {  return HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANTREF;	}

	public String getConstantName() {   return this.m_constantName;   }
	public void setConstantName(String name) {   this.m_constantName = name;   }
	
	@Override
	public HAPElementStructure cloneStructureElement() {
		return new HAPElementStructureLeafConstantReference(this.m_constantName);
	}

	@Override
	public HAPElementStructure solidateConstantScript(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) {  return this;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONSTANT, this.getConstantName());
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPElementStructureLeafConstantReference) {
			HAPElementStructureLeafConstantReference ele = (HAPElementStructureLeafConstantReference)obj;
			if(!HAPUtilityBasic.isEquals(this.getConstantName(), ele.getConstantName()))  return false;
			out = true;
		}
		return out;
	}	
}
