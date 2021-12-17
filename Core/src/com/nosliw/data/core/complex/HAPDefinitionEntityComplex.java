package com.nosliw.data.core.complex;

import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.common.HAPWithValueStructure;
import com.nosliw.data.core.complex.valuestructure.HAPPartComplexValueStructure;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

//entity that have data value structure and attachment
public class HAPDefinitionEntityComplex implements HAPDefinitionEntityInDomain, HAPWithValueStructure, HAPWithAttachment{

	private Map<String, HAPIdEntityInDomain> m_attributesRef;
	
	private String m_entityType;
	
	private String m_entityId;
	
	public HAPDefinitionEntityComplex (String entityType) {
		this.m_entityType = entityType;
	}
	
	@Override
	public String getEntityType() {  return this.m_entityType;	}

	@Override
	public String getEntityId() {    return this.m_entityId;    }
	
	public void setEntityId(String entityId) {    this.m_entityId = entityId;     }

	public HAPIdEntityInDomain getAttributeReferenceId(String attributeName) {
		return this.m_attributesRef.get(attributeName);
	}

	public void setAttributeReferenceId(String attributeName, HAPIdEntityInDomain entityId) {
		this.m_attributesRef.put(attributeName, entityId);
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
