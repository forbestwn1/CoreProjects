package com.nosliw.core.application.division.manual;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPIdBrick;

public class HAPManualInfoAttributeValueReferenceEntity extends HAPManualInfoAttributeValueWithEntity{

	//reference to attachment
	public static final String ENTITYREFERENCE = "entityReference";

	//reference to local resource
	private HAPIdBrick m_localEntityId;

	private HAPManualEntity m_entity;

	public HAPManualInfoAttributeValueReferenceEntity(HAPIdBrick localEntityId) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_ENTITYREFERENCE, localEntityId.getEntityTypeId());
		this.m_localEntityId = localEntityId;
	}

	public void setReferencedEntity(HAPManualEntity entity) {
		this.m_entity = entity;
	}
	
}
