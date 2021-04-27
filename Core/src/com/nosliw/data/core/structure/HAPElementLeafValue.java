package com.nosliw.data.core.structure;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPElementLeafValue extends HAPElementLeafVariable{

	@Override
	public String getType() {	return HAPConstantShared.CONTEXT_ELEMENTTYPE_VALUE;	}

	@Override
	public HAPElement cloneContextDefinitionElement() {
		HAPElementLeafValue out = new HAPElementLeafValue();
		this.toContextDefinitionElement(out);
		return out;
	}

	@Override
	public HAPElement toSolidContextDefinitionElement(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) { return this; }

	@Override
	public void toContextDefinitionElement(HAPElement out) {
		super.toContextDefinitionElement(out);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPElementLeafValue) {
			out = true;
		}
		return out;
	}
}
