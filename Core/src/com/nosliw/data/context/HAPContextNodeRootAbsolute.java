package com.nosliw.data.context;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;

public class HAPContextNodeRootAbsolute extends HAPContextNodeRoot{

	@Override
	public String getType() {		return HAPConstant.UIRESOURCE_ROOTTYPE_ABSOLUTE;	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
}
