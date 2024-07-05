package com.nosliw.core.application.common.structure;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

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
	public void discoverConstantScript(HAPIdEntityInDomain complexEntityId, HAPContextParser parserContext, HAPParserDataExpression expressionParser) {	}

	@Override
	public void solidateConstantScript(Map<String, String> values) {}

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
