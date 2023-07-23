package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public class HAPInfoAdapterDefinition extends HAPInfoAdapter{

	public HAPInfoAdapterDefinition(String valueType, HAPIdEntityInDomain adapterEntityId) {
		super(valueType, adapterEntityId);
	}

	public HAPIdEntityInDomain getEntityIdValue() {   return  (HAPIdEntityInDomain)this.getValue();    }
}
