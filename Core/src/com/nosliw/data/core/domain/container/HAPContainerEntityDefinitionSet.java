package com.nosliw.data.core.domain.container;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPContainerEntityDefinitionSet<T extends HAPElementContainerDefinition> extends HAPContainerEntityDefinition<T>{

	public HAPContainerEntityDefinitionSet() {}

	public HAPContainerEntityDefinitionSet(String eleType) {
		super(eleType);
	}

	@Override
	public String getContainerType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_DEFINITION_SET; }

	@Override
	public HAPContainerEntityDefinitionSet cloneContainerEntity() {
		HAPContainerEntityDefinitionSet out = new HAPContainerEntityDefinitionSet();
		this.cloneToContainer(out);
		return out;
	}

}
