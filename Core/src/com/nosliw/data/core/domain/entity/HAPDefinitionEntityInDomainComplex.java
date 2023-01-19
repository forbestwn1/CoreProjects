package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.component.HAPWithAttachment;

//entity that have data value structure and attachment
public abstract class HAPDefinitionEntityInDomainComplex extends HAPDefinitionEntityInDomain implements HAPWithValueContext, HAPWithAttachment{

	protected HAPDefinitionEntityInDomainComplex() {}
	
	public HAPDefinitionEntityInDomainComplex (String entityType) {
		super(entityType);
	}
	
	@Override
	public HAPEmbededDefinitionWithId getValueContextEntity() {  		return this.getNormalAttributeValueWithId(HAPWithValueContext.VALUECONTEXT);	}

	@Override
	public void setValueContextEntity(HAPEmbededDefinitionWithId valueContextEntity) {    this.setNormalAttribute(HAPWithValueContext.VALUECONTEXT, valueContextEntity);      }

	@Override
	public String getValueStructureTypeIfNotDefined() {  return null;  }

	@Override
	public HAPEmbededDefinitionWithId getAttachmentContainerEntity() {  return this.getNormalAttributeValueWithId(HAPWithAttachment.ATTACHMENT);  }

	@Override
	public void setAttachmentContainerEntity(HAPEmbededDefinitionWithId attachmentEntity) {    this.setNormalAttribute(HAPWithAttachment.ATTACHMENT, attachmentEntity);   }

}
