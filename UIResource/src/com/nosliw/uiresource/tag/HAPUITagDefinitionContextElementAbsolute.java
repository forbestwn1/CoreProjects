package com.nosliw.uiresource.tag;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.uiresource.context.HAPContextNodeRootAbsolute;

public class HAPUITagDefinitionContextElementAbsolute extends HAPContextNodeRootAbsolute implements HAPUITagDefinitionContextElment{

	@Override
	public String getType() {		return HAPConstant.UIRESOURCE_ROOTTYPE_ABSOLUTE;	}

}
