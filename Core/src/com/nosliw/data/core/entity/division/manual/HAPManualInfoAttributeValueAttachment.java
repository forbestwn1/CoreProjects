package com.nosliw.data.core.entity.division.manual;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.attachment.HAPReferenceAttachment;
import com.nosliw.data.core.entity.HAPInfoEntityType;

public class HAPManualInfoAttributeValueAttachment extends HAPManualInfoAttributeValue{

	//reference to attachment
	public static final String REFERENCE = "reference";

	//reference to data in attachment
	private HAPReferenceAttachment m_reference;
	
	public HAPManualInfoAttributeValueAttachment(HAPInfoEntityType entityTypeInfo, HAPReferenceAttachment reference) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_ATTACHMENTREFERENCE, entityTypeInfo);
		this.m_reference = reference;
	}

	
	
}
