package com.nosliw.data.core.domain.container;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPContainerEntityDefinitionList<T extends HAPElementContainerDefinition> extends HAPContainerEntityDefinition<T>{

	public HAPContainerEntityDefinitionList() {}

	@Override
	public String getContainerType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_DEFINITION_LIST; }

	public void addEntityElement(T ele, int index) {
		this.m_eleById.put(ele.getElementId(), ele);
		this.m_eleArray.add(index, ele);
	}

	@Override
	public HAPContainerEntityDefinitionList cloneContainerEntity() {
		HAPContainerEntityDefinitionList out = new HAPContainerEntityDefinitionList();
		this.cloneToContainer(out);
		return out;
	}

}
