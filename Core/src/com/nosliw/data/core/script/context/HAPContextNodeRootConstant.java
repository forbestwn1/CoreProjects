package com.nosliw.data.core.script.context;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;

public class HAPContextNodeRootConstant  extends HAPContextNodeRoot{

	@Override
	public String getType() {		return HAPConstant.UIRESOURCE_ROOTTYPE_CONSTANT;	}

	@Override
	public HAPContextNodeRoot toSolidContextNode(Map<String, Object> constants, HAPEnvContextProcessor contextProcessorEnv) {
		HAPContextNodeRootConstant out = new HAPContextNodeRootConstant();
		this.toSolidContextNode(out, constants, contextProcessorEnv);
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
}
