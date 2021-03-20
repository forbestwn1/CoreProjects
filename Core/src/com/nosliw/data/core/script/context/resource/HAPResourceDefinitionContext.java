package com.nosliw.data.core.script.context.resource;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceDefinitionImp;
import com.nosliw.data.core.script.context.HAPContext;

public class HAPResourceDefinitionContext extends HAPResourceDefinitionImp{

	private HAPContext m_context;
	
	public HAPResourceDefinitionContext(HAPContext context) {
		this.m_context = context;
	}
	
	@Override
	public String getResourceType() {   return HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONTEXT;  }

	public HAPContext getContext() {    return this.m_context;     }
	
}
