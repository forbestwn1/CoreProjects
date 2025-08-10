package com.nosliw.core.xxx.application.division.manual.core.definition1;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionReferenceAttachment;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperValueWithBrick;

public class HAPManualDefinitionWrapperValueReferenceAttachment extends HAPManualDefinitionWrapperValueWithBrick{

	//reference to attachment
	public static final String REFERENCE = "reference";

	//reference to data in attachment
	private HAPManualDefinitionReferenceAttachment m_reference;
	
	public HAPManualDefinitionWrapperValueReferenceAttachment(HAPManualDefinitionReferenceAttachment reference) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_ATTACHMENTREFERENCE, entityTypeInfo);
		this.m_reference = reference;
	}

	
	
}
