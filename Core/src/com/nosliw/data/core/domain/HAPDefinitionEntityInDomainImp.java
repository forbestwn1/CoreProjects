package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoImp;

public class HAPDefinitionEntityInDomainImp extends HAPEntityInfoImp implements HAPDefinitionEntityInDomain{

	//simple attribute by name
	private Map<String, HAPIdEntityInDomain> m_attributesSimple;
	
	//container attribute by name
	private Map<String, HAPContainerEntity> m_attributeContainer;
	
	private String m_entityType;
	
	protected HAPDefinitionEntityInDomainImp() {}
	
	public HAPDefinitionEntityInDomainImp (String entityType) {
		this.m_entityType = entityType;
		this.m_attributesSimple = new LinkedHashMap<String, HAPIdEntityInDomain>();
		this.m_attributeContainer = new LinkedHashMap<String, HAPContainerEntity>();
	}

	@Override
	public boolean isComplexEntity() {    return false;   }

	@Override
	public String getEntityType() {  return this.m_entityType;	}

	public HAPIdEntityInDomain getSimpleAttribute(String attributeName) {		return this.m_attributesSimple.get(attributeName);	}

	public void setSimpleAttribute(String attributeName, HAPIdEntityInDomain entityId) {		this.m_attributesSimple.put(attributeName, entityId);	}
	
	public Map<String, HAPIdEntityInDomain> getSimpleAttributes(){    return this.m_attributesSimple;     }
	
	public Map<String, HAPContainerEntity> getContainerAttributes(){    return this.m_attributeContainer;     }
	
	public HAPIdEntityInDomain getContainerAttributeElement(String attributeName, HAPInfoContainerElement eleInfo) {
		return this.m_attributeContainer.get(attributeName).getElement(eleInfo);
	}
	
	public void addContainerElementAttribute(HAPIdEntityInDomain eleEntityId, String attributeName, HAPInfoContainerElement eleInfo) {
		HAPContainerEntity container = this.m_attributeContainer.get(attributeName);
		if(container==null) {
			container = new HAPContainerEntity();
			this.m_attributeContainer.put(attributeName, container);
		}
		container.addEntityElement(eleEntityId, eleInfo);
	}
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityInDomainImp out = new HAPDefinitionEntityInDomainImp();
		this.cloneToDefinitionEntityInDomainImp(out);
		return out;
	}

	protected void cloneToDefinitionEntityInDomainImp(HAPDefinitionEntityInDomainImp entityDefinitionInDomainImp) {
		this.cloneToEntityInfo(entityDefinitionInDomainImp);
		entityDefinitionInDomainImp.m_entityType = this.m_entityType;
		for(String attrName : this.m_attributesSimple.keySet()) {
			entityDefinitionInDomainImp.m_attributesSimple.put(attrName, this.m_attributesSimple.get(attrName));
		}
		
		for(String attrName : this.m_attributeContainer.keySet()) {
			entityDefinitionInDomainImp.m_attributeContainer.put(attrName, this.m_attributeContainer.get(attrName));
		}
	}
}
