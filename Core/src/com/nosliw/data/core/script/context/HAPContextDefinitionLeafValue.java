package com.nosliw.data.core.script.context;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPContextDefinitionLeafValue extends HAPContextDefinitionLeafVariable{

	@Override
	public String getType() {	return HAPConstant.CONTEXT_ELEMENTTYPE_VALUE;	}

	@Override
	public HAPContextDefinitionElement cloneContextDefinitionElement() {
		HAPContextDefinitionLeafValue out = new HAPContextDefinitionLeafValue();
		this.toContextDefinitionElement(out);
		return out;
	}

	@Override
	public HAPContextDefinitionElement toSolidContextDefinitionElement(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) { return this; }

	@Override
	public void toContextDefinitionElement(HAPContextDefinitionElement out) {
		super.toContextDefinitionElement(out);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPContextDefinitionLeafValue) {
			out = true;
		}
		return out;
	}
}
