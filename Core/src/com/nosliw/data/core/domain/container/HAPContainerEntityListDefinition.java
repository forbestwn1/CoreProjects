package com.nosliw.data.core.domain.container;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPContainerEntityListDefinition extends HAPContainerEntityList<HAPInfoDefinitionContainerElementList>{

	public HAPContainerEntityListDefinition() {}
	
	@Override
	public String getContainerType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_DEFINITION_LIST; }

	@Override
	public HAPContainerEntityListDefinition cloneContainerEntity() {
		HAPContainerEntityListDefinition out = new HAPContainerEntityListDefinition();
		this.cloneToContainer(out);
		return out;
	}
}
