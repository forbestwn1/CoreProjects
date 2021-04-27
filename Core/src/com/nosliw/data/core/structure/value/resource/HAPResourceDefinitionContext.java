package com.nosliw.data.core.structure.value.resource;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceDefinitionImp;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinitionFlat;

public class HAPResourceDefinitionContext extends HAPResourceDefinitionImp{

	private HAPContextStructureValueDefinitionFlat m_context;
	
	public HAPResourceDefinitionContext(HAPContextStructureValueDefinitionFlat context) {
		this.m_context = context;
	}
	
	@Override
	public String getResourceType() {   return HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONTEXT;  }

	public HAPContextStructureValueDefinitionFlat getContext() {    return this.m_context;     }
	
}
