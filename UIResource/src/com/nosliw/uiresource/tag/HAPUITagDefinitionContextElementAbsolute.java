package com.nosliw.uiresource.tag;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.uiresource.context.HAPContextNodeRootAbsolute;

public class HAPUITagDefinitionContextElementAbsolute extends HAPContextNodeRootAbsolute implements HAPUITagDefinitionContextElment{

	@Override
	public String getType() {		return HAPConstant.UIRESOURCE_ROOTTYPE_ABSOLUTE;	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(HAPUITagDefinitionContextElment.TYPE, this.getType());
		
	}
}
