package com.nosliw.core.application.division.manual;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.attachment.HAPReferenceAttachment;

public class HAPManualWrapperValueInAttributeReferenceAttachment extends HAPManualWrapperValueInAttributeWithBrick{

	//reference to attachment
	public static final String REFERENCE = "reference";

	//reference to data in attachment
	private HAPReferenceAttachment m_reference;
	
	public HAPManualWrapperValueInAttributeReferenceAttachment(HAPReferenceAttachment reference) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_ATTACHMENTREFERENCE, entityTypeInfo);
		this.m_reference = reference;
	}

	
	
}