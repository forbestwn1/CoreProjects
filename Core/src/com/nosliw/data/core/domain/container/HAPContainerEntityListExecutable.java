package com.nosliw.data.core.domain.container;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPContainerEntityListExecutable extends HAPContainerEntityList<HAPInfoDefinitionContainerElementList>{

	public HAPContainerEntityListExecutable() {	}
	
	@Override
	public String getContainerType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_EXECUTABLE_LIST; }

	@Override
	public HAPContainerEntity<HAPInfoDefinitionContainerElementList> cloneContainerEntity() {
		HAPContainerEntityListExecutable out = new HAPContainerEntityListExecutable();
		this.cloneToContainer(out);
		return out;
	}
}
