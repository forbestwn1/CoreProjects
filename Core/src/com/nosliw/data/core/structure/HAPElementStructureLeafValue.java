package com.nosliw.data.core.structure;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPElementStructureLeafValue extends HAPElementStructureLeafVariable{

	@Override
	public String getType() {	return HAPConstantShared.CONTEXT_ELEMENTTYPE_VALUE;	}

	@Override
	public HAPElementStructure cloneStructureElement() {
		HAPElementStructureLeafValue out = new HAPElementStructureLeafValue();
		this.toStructureElement(out);
		return out;
	}

	@Override
	public HAPElementStructure solidateConstantScript(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) { return this; }

	@Override
	public void toStructureElement(HAPElementStructure out) {
		super.toStructureElement(out);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPElementStructureLeafValue) {
			out = true;
		}
		return out;
	}
}
