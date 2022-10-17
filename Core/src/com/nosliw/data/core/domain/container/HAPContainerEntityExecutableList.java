package com.nosliw.data.core.domain.container;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPContainerEntityExecutableList<T extends HAPElementContainerExecutable> extends HAPContainerEntityExecutable<T>{

	public HAPContainerEntityExecutableList() {	}

	public HAPContainerEntityExecutableList(String eleType) {
		super(eleType);
	}

	public void addEntityElement(T ele, int index) {
		this.m_eleById.put(ele.getElementId(), ele);
		this.m_eleArray.add(index, ele);
	}

	@Override
	public String getContainerType() {   return HAPConstantShared.ENTITYCONTAINER_TYPE_EXECUTABLE_LIST;  }

	@Override
	public HAPContainerEntity cloneContainerEntity() {
		HAPContainerEntityExecutableList out = new HAPContainerEntityExecutableList();
		this.cloneToContainer(out);
		return out;
	}
}
