package com.nosliw.data.core.script.context;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;

public class HAPContextDefinitionUnknown extends HAPContextDefinitionElement{

	@Override
	public String getType() {		return HAPConstant.CONTEXT_ELEMENTTYPE_UNKNOW;	}

	@Override
	public HAPContextDefinitionElement cloneContextDefinitionElement() {
		return new HAPContextDefinitionUnknown();
	}

	@Override
	public HAPContextDefinitionElement toSolidContextDefinitionElement(Map<String, Object> constants,
			HAPEnvContextProcessor contextProcessorEnv) {
		return this.cloneContextDefinitionElement();
	}
}
