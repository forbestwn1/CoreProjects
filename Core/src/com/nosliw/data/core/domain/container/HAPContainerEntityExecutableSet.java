package com.nosliw.data.core.domain.container;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPContainerEntityExecutableSet<T extends HAPElementContainerExecutable> extends HAPContainerEntityExecutable<T>{

	public HAPContainerEntityExecutableSet() {	}

	public HAPContainerEntityExecutableSet(String eleType) {
		super(eleType);
	}

	@Override
	public String getContainerType() {   return HAPConstantShared.ENTITYCONTAINER_TYPE_EXECUTABLE_SET;  }

	@Override
	public HAPContainerEntity cloneContainerEntity() {
		HAPContainerEntityExecutableSet out = new HAPContainerEntityExecutableSet();
		this.cloneToContainer(out);
		return out;
	}
}
