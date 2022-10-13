package com.nosliw.data.core.domain.container;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPContainerEntitySetDefinition extends HAPContainerEntitySet<HAPInfoDefinitionContainerElementSet>{

	public HAPContainerEntitySetDefinition() {}
	
	@Override
	public String getContainerType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_DEFINITION_SET; }

	@Override
	public HAPContainerEntitySetDefinition cloneContainerEntity() {
		HAPContainerEntitySetDefinition out = new HAPContainerEntitySetDefinition();
		this.cloneToContainer(out);
		return out;
	}
}
