package com.nosliw.core.application.common.structure22;

import com.nosliw.common.utils.HAPConstantShared;

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
	public void toStructureElement(HAPElementStructure out) {
		super.toStructureElement(out);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			return false;
		}

		boolean out = false;
		if(obj instanceof HAPElementStructureLeafValue) {
			out = true;
		}
		return out;
	}
}
