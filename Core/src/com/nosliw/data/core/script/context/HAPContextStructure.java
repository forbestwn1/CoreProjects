package com.nosliw.data.core.script.context;

import com.nosliw.common.utils.HAPConstant;

public interface HAPContextStructure {

	String getType();
	
	boolean isFlat();

	HAPContextDefinitionRoot getElement(String eleName);
	
	HAPContextStructure cloneContextStructure();
	
	public static HAPContextGroup toContextGroup(HAPContextStructure context) {
		HAPContextGroup out = null;
		if(context instanceof HAPContextGroup)  out = (HAPContextGroup)context;
		else if(context instanceof HAPContext) {
			out = new HAPContextGroup();
			out.setContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC, (HAPContext)context);
		}
		return out;
	}
	
}
