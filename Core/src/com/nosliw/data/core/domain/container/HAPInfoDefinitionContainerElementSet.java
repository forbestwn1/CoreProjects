package com.nosliw.data.core.domain.container;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPEmbededWithId;

public class HAPInfoDefinitionContainerElementSet extends HAPInfoDefinitionContainerElement implements HAPInfoContainerElementSet<HAPEmbededWithId>{

	public HAPInfoDefinitionContainerElementSet(HAPEmbededWithId embededWithId) {
		super(embededWithId);
	}

	public HAPInfoDefinitionContainerElementSet() {	}

	@Override
	public String getInfoType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_DEFINITION_SET;    }

	@Override
	public HAPInfoDefinitionContainerElementSet cloneContainerElementInfo() {
		HAPInfoDefinitionContainerElementSet out = new HAPInfoDefinitionContainerElementSet();
		this.cloneToInfoContainerElement(out);
		return out;
	}

	public HAPInfoDefinitionContainerElementSet cloneContainerElementInfoSet() {	return this.cloneContainerElementInfo();	}
}
