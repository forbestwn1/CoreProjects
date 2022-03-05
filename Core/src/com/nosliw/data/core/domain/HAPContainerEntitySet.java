package com.nosliw.data.core.domain;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPContainerEntitySet extends HAPContainerEntityImp<HAPInfoContainerElementSet>{

	public HAPContainerEntitySet() {
	}
	
	@Override
	public String getContainerType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_SET; }

	@Override
	public HAPContainerEntity<HAPInfoContainerElementSet> cloneContainerEntity() {
		HAPContainerEntitySet out = new HAPContainerEntitySet();
		this.cloneToContainer(out);
		return out;
	}
}
