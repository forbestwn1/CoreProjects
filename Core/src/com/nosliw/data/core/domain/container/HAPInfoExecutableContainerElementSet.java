package com.nosliw.data.core.domain.container;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPEmbededWithExecutable;

public class HAPInfoExecutableContainerElementSet extends HAPInfoExecutableContainerElement implements HAPInfoContainerElementSet<HAPEmbededWithExecutable>{

	public HAPInfoExecutableContainerElementSet(HAPEmbededWithExecutable embededEntity, String elementId) {
		super(embededEntity, elementId);
	}

	public HAPInfoExecutableContainerElementSet() {	}

	@Override
	public String getInfoType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_EXECUTABLE_SET;    }

	@Override
	public HAPInfoExecutableContainerElementSet cloneContainerElementInfo() {
		HAPInfoExecutableContainerElementSet out = new HAPInfoExecutableContainerElementSet();
		this.cloneToInfoContainerElement(out);
		return out;
	}
}
