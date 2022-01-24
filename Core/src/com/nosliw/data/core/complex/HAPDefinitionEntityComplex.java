package com.nosliw.data.core.complex;

import com.nosliw.data.core.common.HAPWithValueStructure;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomainImp;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

//entity that have data value structure and attachment
public class HAPDefinitionEntityComplex extends HAPDefinitionEntityInDomainImp implements HAPWithValueStructure, HAPWithAttachment{

	protected HAPDefinitionEntityComplex() {}
	
	public HAPDefinitionEntityComplex (String entityType) {
		super(entityType);
	}
	
	@Override
	public boolean isComplexEntity() {    return true;   }

	@Override
	public HAPIdEntityInDomain getValueStructureComplexId() {  return this.getSimpleAttribute(HAPWithValueStructure.VALUESTRUCTURE);  }

	@Override
	public void setValueStructureComplexId(HAPIdEntityInDomain valueStructureComplexId) {    this.setSimpleAttribute(HAPWithValueStructure.VALUESTRUCTURE, valueStructureComplexId);      }

	@Override
	public String getValueStructureTypeIfNotDefined() {  return null;  }

	@Override
	public HAPIdEntityInDomain getAttachmentContainerId() {   return this.getSimpleAttribute(HAPWithAttachment.ATTACHMENT);  }

	@Override
	public void setAttachmentContainerId(HAPIdEntityInDomain attachmentId) {    this.setSimpleAttribute(HAPWithAttachment.ATTACHMENT, attachmentId);   }

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityComplex out = new HAPDefinitionEntityComplex();
		this.cloneToDefinitionEntityInDomainImp(out);
		return out;
	}
}
