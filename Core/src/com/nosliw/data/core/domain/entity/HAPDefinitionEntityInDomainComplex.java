package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

//entity that have data value structure and attachment
public abstract class HAPDefinitionEntityInDomainComplex extends HAPDefinitionEntityInDomain implements HAPWithValueContext, HAPWithAttachment{

	protected HAPDefinitionEntityInDomainComplex() {}
	
	public HAPDefinitionEntityInDomainComplex (String entityType) {
		super(entityType);
	}
	
	@Override
	public HAPIdEntityInDomain getValueContextEntity() {  	return (HAPIdEntityInDomain)this.getNormalAttributeValue(HAPWithValueContext.VALUECONTEXT);	}

	@Override
	public void setValueContextEntity(HAPIdEntityInDomain valueContextEntity) {    this.setNormalAttributeValueSimple(HAPWithValueContext.VALUECONTEXT, valueContextEntity);      }

	@Override
	public String getValueStructureTypeIfNotDefined() {  return null;  }

	@Override
	public HAPIdEntityInDomain getAttachmentContainerEntity() {  return (HAPIdEntityInDomain)this.getNormalAttributeValue(HAPWithAttachment.ATTACHMENT);  }

	@Override
	public void setAttachmentContainerEntity(HAPIdEntityInDomain attachmentEntity) {    this.setNormalAttributeValueSimple(HAPWithAttachment.ATTACHMENT, attachmentEntity);   }

}
