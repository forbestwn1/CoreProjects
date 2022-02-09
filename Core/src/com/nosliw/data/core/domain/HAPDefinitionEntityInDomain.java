package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.utils.HAPConstantShared;

public abstract class HAPDefinitionEntityInDomain implements HAPEntityOrReference{

	//simple attribute by name
	private Map<String, HAPIdEntityInDomain> m_attributesSimple;
	
	//container attribute by name
	private Map<String, HAPContainerEntity> m_attributeContainer;
	
	private String m_entityType;
	
	protected HAPDefinitionEntityInDomain() {}
	
	public HAPDefinitionEntityInDomain (String entityType) {
		this.m_entityType = entityType;
		this.m_attributesSimple = new LinkedHashMap<String, HAPIdEntityInDomain>();
		this.m_attributeContainer = new LinkedHashMap<String, HAPContainerEntity>();
	}

	public abstract boolean isComplexEntity();
	
	public abstract HAPEntityOrReference getChild(String childName);

	public abstract HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain();

	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.ENTITY;    }

	public String getEntityType() {  return this.m_entityType;	}

	public HAPIdEntityInDomain getSimpleAttribute(String attributeName) {		return this.m_attributesSimple.get(attributeName);	}

	public void setSimpleAttribute(String attributeName, HAPIdEntityInDomain entityId) {		this.m_attributesSimple.put(attributeName, entityId);	}
	
	public Map<String, HAPIdEntityInDomain> getSimpleAttributes(){    return this.m_attributesSimple;     }
	
	public Map<String, HAPContainerEntity> getContainerAttributes(){    return this.m_attributeContainer;     }
	
	public HAPIdEntityInDomain getContainerAttributeElement(String attributeName, HAPInfoContainerElement eleInfo) {
		return this.m_attributeContainer.get(attributeName).getElement(eleInfo);
	}
	
	public void addContainerElementAttribute(String attributeName, HAPInfoContainerElement eleInfo) {
		HAPContainerEntity container = this.m_attributeContainer.get(attributeName);
		if(container==null) {
			String eleInfoType = eleInfo.getInfoType();
			if(HAPConstantShared.ENTITYCONTAINER_TYPE_SET.equals(eleInfoType)) {
				container = new HAPContainerEntitySet();
			}
			else if(HAPConstantShared.ENTITYCONTAINER_TYPE_LIST.equals(eleInfoType)) {
				container = new HAPContainerEntityList();
				
			}
			this.m_attributeContainer.put(attributeName, container);
		}
		container.addEntityElement(eleInfo);
	}

	protected void cloneToDefinitionEntityInDomain(HAPDefinitionEntityInDomain entityDefinitionInDomainImp) {
		entityDefinitionInDomainImp.m_entityType = this.m_entityType;
		for(String attrName : this.m_attributesSimple.keySet()) {
			entityDefinitionInDomainImp.m_attributesSimple.put(attrName, this.m_attributesSimple.get(attrName));
		}
		
		for(String attrName : this.m_attributeContainer.keySet()) {
			entityDefinitionInDomainImp.m_attributeContainer.put(attrName, this.m_attributeContainer.get(attrName));
		}
	}
}
