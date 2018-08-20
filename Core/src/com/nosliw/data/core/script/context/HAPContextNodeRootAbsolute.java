package com.nosliw.data.core.script.context;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;

public class HAPContextNodeRootAbsolute extends HAPContextNodeRootVariable{

	@Override
	public String getType() {		return HAPConstant.UIRESOURCE_ROOTTYPE_ABSOLUTE;	}

	@Override
	public HAPContextNodeRoot cloneContextNodeRoot() {
		HAPContextNodeRootAbsolute out = new HAPContextNodeRootAbsolute();
		this.toContextNodeRootVariable(out);
		return out;
	}

	@Override
	public HAPContextNodeRoot toSolidContextNodeRoot(Map<String, Object> constants, HAPEnvContextProcessor contextProcessorEnv) {
		HAPContextNodeRootAbsolute out = new HAPContextNodeRootAbsolute();
		this.toSolidContextNode(out, constants, contextProcessorEnv);
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}

}
