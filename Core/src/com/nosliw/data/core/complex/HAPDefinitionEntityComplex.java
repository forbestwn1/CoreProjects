package com.nosliw.data.core.complex;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.common.HAPWithValueStructure;
import com.nosliw.data.core.complex.valuestructure.HAPPartComplexValueStructure;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.domain.HAPContainerEntity;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

//entity that have data value structure and attachment
public class HAPDefinitionEntityComplex extends HAPEntityInfoImp implements HAPDefinitionEntityInDomain, HAPWithValueStructure, HAPWithAttachment{

	//simple attribute by name
	private Map<String, HAPIdEntityInDomain> m_attributesSimple;
	
	//container attribute by name
	private Map<String, HAPContainerEntity> m_attributeContainer;
	
	private String m_entityType;
	
	public HAPDefinitionEntityComplex (String entityType) {
		this.m_entityType = entityType;
		
	}
	
	@Override
	public String getEntityType() {  return this.m_entityType;	}

	public HAPIdEntityInDomain getSimpleAttribute(String attributeName) {		return this.m_attributesSimple.get(attributeName);	}

	public void setSimpleAttribute(String attributeName, HAPIdEntityInDomain entityId) {		this.m_attributesSimple.put(attributeName, entityId);	}
	
	public void addContainerElementAttribute(String attributeName, HAPIdEntityInDomain entityId, String entityName) {
		
	}
	
	public Map<String, HAPIdEntityInDomain> getSimpleAttributes(){    return this.m_attributesSimple;     }
	
	public Map<String, HAPContainerEntity> getContainerAttributes(){    return this.m_attributeContainer;     }
	
	public HAPIdEntityInDomain getContainerAttributeElement(String attributeName, String eleName) {
		
	}
	
	public HAPIdEntityInDomain getContainerAttributeElement(String attributeName, String eleName) {
		
	}
	
	@Override
	public HAPIdEntityInDomain getValueStructureComplexId() {  return this.getAttributeReferenceId(HAPWithValueStructure.VALUESTRUCTURE);  }

	@Override
	public void setValueStructureComplexId(HAPIdEntityInDomain valueStructureComplexId) {    this.setAttributeReferenceId(HAPWithValueStructure.VALUESTRUCTURE, valueStructureComplexId);      }

	@Override
	public String getValueStructureTypeIfNotDefined() {  return null;  }

	@Override
	public HAPIdEntityInDomain getAttachmentContainerId() {   return this.getAttributeReferenceId(HAPWithAttachment.ATTACHMENT);  }

	@Override
	public void setAttachmentContainerId(HAPIdEntityInDomain attachmentId) {    this.setAttributeReferenceId(HAPWithAttachment.ATTACHMENT, attachmentId);   }

	

	@Override
	public void cloneToComplexEntityDefinition(HAPDefinitionEntityComplex complexEntity, boolean cloneValueStructure) {
		this.cloneToEntityInfo(complexEntity);
		this.cloneToResourceDefinition(complexEntity);
		this.cloneToAttachment(complexEntity);

		if(cloneValueStructure) {
			for(HAPPartComplexValueStructure part : this.getValueStructureComplex().getParts()) {
				complexEntity.getValueStructureComplex().addPart(part);
			}
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUESTRUCTURE, HAPJsonUtility.buildJson(this.m_valueStructureComplex, HAPSerializationFormat.JSON));
		jsonMap.put(HAPWithAttachment.ATTACHMENT, this.getAttachmentContainer().toStringValue(HAPSerializationFormat.JSON));
	}

}
