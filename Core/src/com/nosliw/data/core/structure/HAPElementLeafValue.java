package com.nosliw.data.core.structure;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPElementLeafValue extends HAPElementLeafVariable{

	@Override
	public String getType() {	return HAPConstantShared.CONTEXT_ELEMENTTYPE_VALUE;	}

	@Override
	public HAPElement cloneStructureElement() {
		HAPElementLeafValue out = new HAPElementLeafValue();
		this.toStructureElement(out);
		return out;
	}

	@Override
	public HAPElement solidateConstantScript(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) { return this; }

	@Override
	public void toStructureElement(HAPElement out) {
		super.toStructureElement(out);
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
