package com.nosliw.data.core.domain.entity;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

@HAPEntityWithAttribute
public class HAPEmbededExecutableWithId extends HAPEmbededExecutable{

	public HAPEmbededExecutableWithId() {}
	
	public HAPEmbededExecutableWithId(HAPIdEntityInDomain entityId, boolean isComplex) {
		super(entityId, entityId.getEntityType(), isComplex);
	}
	
	@Override
	public String getType() {  return HAPConstantShared.EMBEDED_TYPE_EXECUTABLE_ID;  }
	
	public HAPIdEntityInDomain getEntityId() {	return (HAPIdEntityInDomain)this.getValue();	}
	
	public void setEntityId(HAPIdEntityInDomain entityId) {  this.setValue(entityId);  }

	@Override
	public HAPEmbededExecutableWithId cloneEmbeded() {
		HAPEmbededExecutableWithId out = new HAPEmbededExecutableWithId();
		this.cloneToEmbeded(out);
		return out;
	}
	
}
