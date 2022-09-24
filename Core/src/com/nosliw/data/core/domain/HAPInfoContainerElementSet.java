package com.nosliw.data.core.domain;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPInfoContainerElementSet extends HAPInfoContainerElement{

	public HAPInfoContainerElementSet(HAPEmbeded embededEntity) {
		super(embededEntity);
	}

	public HAPInfoContainerElementSet() {	}

	@Override
	public String getInfoType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_SET;    }

	@Override
	public HAPInfoContainerElement cloneContainerElementInfo() {
		HAPInfoContainerElementSet out = new HAPInfoContainerElementSet();
		this.cloneToInfoContainerElement(out);
		return out;
	}

	public HAPInfoContainerElementSet cloneContainerElementInfoSet() {
		return (HAPInfoContainerElementSet)this.cloneContainerElementInfo();
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		return super.buildObjectByJson(json);
	}
}
