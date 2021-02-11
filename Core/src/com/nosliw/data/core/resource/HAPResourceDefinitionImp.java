package com.nosliw.data.core.resource;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstantShared;

public abstract class HAPResourceDefinitionImp extends HAPSerializableImp implements HAPResourceDefinition{

	private HAPResourceId m_resourceId;
	
	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.ENTITY;    }

	@Override
	public void setResourceId(HAPResourceId resourceId) {   this.m_resourceId = resourceId;  }

	@Override
	public HAPResourceId getResourceId() {   return this.m_resourceId; }

	@Override
	public void cloneToResourceDefinition(HAPResourceDefinition resourceDef) {
		resourceDef.setResourceId(this.getResourceId());
	}

}
