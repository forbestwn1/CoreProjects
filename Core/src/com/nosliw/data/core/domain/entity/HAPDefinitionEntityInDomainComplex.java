package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

//entity that have data value structure and attachment
public abstract class HAPDefinitionEntityInDomainComplex extends HAPDefinitionEntityInDomain implements HAPWithValueContext, HAPWithAttachment{

	protected HAPDefinitionEntityInDomainComplex() {}
	
	protected HAPDefinitionEntityInDomainComplex (String entityType) {
		super(entityType);
	}
	
	@Override
	public HAPIdEntityInDomain getValueContextEntity() {  	return (HAPIdEntityInDomain)this.getAttributeValue(HAPWithValueContext.VALUECONTEXT);	}

	@Override
	public void setValueContextEntity(HAPIdEntityInDomain valueContextEntity) {		this.setAttributeValueSimple(HAPWithValueContext.VALUECONTEXT, valueContextEntity);      }

	@Override
	public HAPIdEntityInDomain getAttachmentContainerEntity() {  return (HAPIdEntityInDomain)this.getAttributeValue(HAPWithAttachment.ATTACHMENT);  }

	@Override
	public void setAttachmentContainerEntity(HAPIdEntityInDomain attachmentEntity) {    this.setAttributeValueSimple(HAPWithAttachment.ATTACHMENT, attachmentEntity);   }

}
