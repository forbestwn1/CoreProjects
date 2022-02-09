package com.nosliw.data.core.domain;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPInfoContainerElementSet extends HAPInfoContainerElement{

	public HAPInfoContainerElementSet(HAPIdEntityInDomain entityId) {
		super(HAPConstantShared.ENTITYCONTAINER_TYPE_SET, entityId);
	}

	public HAPInfoContainerElementSet() {
		super(HAPConstantShared.ENTITYCONTAINER_TYPE_SET);
	}

	@Override
	public HAPInfoContainerElement cloneContainerElementInfo() {
		HAPInfoContainerElementSet out = new HAPInfoContainerElementSet();
		this.cloneToInfoContainerElement(out);
		return out;
	}

}
