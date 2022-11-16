package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.common.HAPWithValueStructure;
import com.nosliw.data.core.component.HAPWithAttachment;

//entity that have data value structure and attachment
public abstract class HAPDefinitionEntityInDomainComplex extends HAPDefinitionEntityInDomain implements HAPWithValueStructure, HAPWithAttachment{

	protected HAPDefinitionEntityInDomainComplex() {}
	
	public HAPDefinitionEntityInDomainComplex (String entityType) {
		super(entityType);
	}
	
	@Override
	public HAPEmbededDefinitionWithId getValueStructureComplexEntity() {  		return this.getNormalAttributeValueWithId(HAPWithValueStructure.VALUESTRUCTURE);	}

	@Override
	public void setValueStructureComplexEntity(HAPEmbededDefinitionWithId valueStructureComplexEntity) {    this.setNormalAttribute(HAPWithValueStructure.VALUESTRUCTURE, valueStructureComplexEntity);      }

	@Override
	public String getValueStructureTypeIfNotDefined() {  return null;  }

	@Override
	public HAPEmbededDefinitionWithId getAttachmentContainerEntity() {  return this.getNormalAttributeValueWithId(HAPWithAttachment.ATTACHMENT);  }

	@Override
	public void setAttachmentContainerEntity(HAPEmbededDefinitionWithId attachmentEntity) {    this.setNormalAttribute(HAPWithAttachment.ATTACHMENT, attachmentEntity);   }

}
