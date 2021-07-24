package com.nosliw.data.core.service.resource;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPPluginResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdLocal;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.service.definition.HAPManagerServiceDefinition;

public class HAPResourceDefinitionPluginServiceDefinition implements HAPPluginResourceDefinition{

	private HAPManagerServiceDefinition m_serviceDefinitionMan;
	
	public HAPResourceDefinitionPluginServiceDefinition(HAPManagerServiceDefinition serviceDefinitionMan) {
		this.m_serviceDefinitionMan = serviceDefinitionMan;
	}
	
	@Override
	public String getResourceType() {  return HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICE;  }

	@Override
	public HAPResourceDefinition getResourceDefinitionBySimpleResourceId(HAPResourceIdSimple resourceId) {
		return this.m_serviceDefinitionMan.getDefinition(new HAPResourceIdServiceDefinition(resourceId).getServiceDefinitionId().getId());
	}

	@Override
	public HAPResourceDefinition parseResourceDefinition(Object content) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPResourceDefinition getResourceDefinitionByLocalResourceId(HAPResourceIdLocal resourceId,
			HAPResourceDefinition relatedResource) {
		// TODO Auto-generated method stub
		return null;
	}
}
