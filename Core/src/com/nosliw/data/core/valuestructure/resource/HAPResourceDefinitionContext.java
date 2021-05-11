package com.nosliw.data.core.valuestructure.resource;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceDefinitionImp;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;

public class HAPResourceDefinitionContext extends HAPResourceDefinitionImp{

	private HAPValueStructureDefinitionFlat m_context;
	
	public HAPResourceDefinitionContext(HAPValueStructureDefinitionFlat context) {
		this.m_context = context;
	}
	
	@Override
	public String getResourceType() {   return HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONTEXT;  }

	public HAPValueStructureDefinitionFlat getContext() {    return this.m_context;     }
	
}
