package com.nosliw.data.core.script.context;

import com.nosliw.common.utils.HAPConstant;

public interface HAPContextStructure {
	
	boolean isFlat();

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
