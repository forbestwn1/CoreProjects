package com.nosliw.data.core.domain.entity;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

@HAPEntityWithAttribute
public class HAPEmbededDefinitionWithId extends HAPEmbededDefinition{

	public HAPEmbededDefinitionWithId() {}
	
	public HAPEmbededDefinitionWithId(HAPIdEntityInDomain entityId, HAPIdEntityInDomain adapterEntityId, boolean isComplex) {
		super(entityId, entityId.getEntityType(), adapterEntityId, isComplex);
	}
	
	@Override
	public String getType() {  return HAPConstantShared.EMBEDED_TYPE_DEFINITION_ID;  }

	public HAPIdEntityInDomain getEntityId() {	return (HAPIdEntityInDomain)this.getValue();	}
	
	public void setEntityId(HAPIdEntityInDomain entityId) {  this.setValue(entityId);  }

	@Override
	public HAPEmbededDefinitionWithId cloneEmbeded() {
		HAPEmbededDefinitionWithId out = new HAPEmbededDefinitionWithId();
		this.cloneToEmbeded(out);
		return out;
	}

}
