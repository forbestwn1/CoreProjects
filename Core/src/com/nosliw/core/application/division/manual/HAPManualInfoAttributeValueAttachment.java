package com.nosliw.core.application.division.manual;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.attachment.HAPReferenceAttachment;

public class HAPManualInfoAttributeValueAttachment extends HAPManualInfoAttributeValueWithEntity{

	//reference to attachment
	public static final String REFERENCE = "reference";

	//reference to data in attachment
	private HAPReferenceAttachment m_reference;
	
	public HAPManualInfoAttributeValueAttachment(HAPReferenceAttachment reference) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_ATTACHMENTREFERENCE, entityTypeInfo);
		this.m_reference = reference;
	}

	
	
}
