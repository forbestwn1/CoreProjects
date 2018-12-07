package com.nosliw.data.core.script.context;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;

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
	public HAPContextDefinitionElement toSolidContextDefinitionElement(Map<String, Object> constants,
			HAPEnvContextProcessor contextProcessorEnv) {
		return this.cloneContextDefinitionElement();
	}

	@Override
	public void toContextDefinitionElement(HAPContextDefinitionElement out) {
		super.toContextDefinitionElement(out);
	}
	
}
