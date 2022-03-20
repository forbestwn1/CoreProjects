package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections4.map.LinkedMap;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;

public abstract class HAPDefinitionEntityInDomain extends HAPSerializableImp implements HAPEntityOrReference{

	public final static String SIMPLEATTRIBUTE = "simpleAttributes"; 
	
	public final static String CONTAINERATTRIBUTE = "containerAttributes"; 
	
	//simple attribute by name
	private Map<String, HAPIdEntityInDomain> m_attributesSimple;
	
	//container attribute by name
	private Map<String, HAPContainerEntity> m_attributeContainer;
	
	private String m_entityType;
	
	protected HAPDefinitionEntityInDomain() {
		this.m_attributesSimple = new LinkedHashMap<String, HAPIdEntityInDomain>();
		this.m_attributeContainer = new LinkedHashMap<String, HAPContainerEntity>();
		this.m_entityType = HAPUtilityDomain.getEntityTypeFromEntityClass(this.getClass());
	}
	
	public HAPDefinitionEntityInDomain (String entityType) {
		this();
		this.m_entityType = entityType;
	}

	public abstract HAPEntityOrReference getChild(String childName);

	public abstract HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain();

	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.ENTITY;    }

	public String getEntityType() {  return this.m_entityType;	}

	public HAPIdEntityInDomain getSimpleAttribute(String attributeName) {		return this.m_attributesSimple.get(attributeName);	}

	public void setSimpleAttribute(String attributeName, HAPIdEntityInDomain entityId) {
		if(entityId==null) {
			this.m_attributesSimple.remove(attributeName);
		}
		else {
			this.m_attributesSimple.put(attributeName, entityId);	
		}
	}
	
	public Map<String, HAPIdEntityInDomain> getSimpleAttributes(){    return this.m_attributesSimple;     }
	
	public Map<String, HAPContainerEntity> getContainerAttributes(){    return this.m_attributeContainer;     }
	
	public HAPIdEntityInDomain getConatinerAttributeElementByName(String attributeName, String eleName) {
		return this.m_attributeContainer.get(attributeName).getElementInfoByName(eleName).getElementEntityId();
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

	public String toExpandedJsonString(HAPDomainDefinitionEntity entityDefDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedMap<String, Class<?>>(); 
		this.buildExpandedJsonMap(jsonMap, typeJsonMap, entityDefDomain);
		return HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap);
	}
	
	//normal json
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		
		for(String attrName : this.m_attributesSimple.keySet()) {
			jsonMap.put(attrName, this.m_attributesSimple.get(attrName).toStringValue(HAPSerializationFormat.JSON));
		}
		
		for(String attrName : this.m_attributeContainer.keySet()) {
			jsonMap.put(attrName, this.m_attributeContainer.get(attrName).toStringValue(HAPSerializationFormat.JSON));
		}
	}

	//expanded json. expand all referenced 
	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainDefinitionEntity entityDefDomain){
		for(String attrName : this.m_attributesSimple.keySet()) {
			HAPIdEntityInDomain entityId = this.m_attributesSimple.get(attrName);
			jsonMap.put(attrName, entityDefDomain.getEntityInfo(entityId).toExpandedJsonString(entityDefDomain));
		}
		
		for(String attrName : this.m_attributeContainer.keySet()) {
			jsonMap.put(attrName, this.m_attributeContainer.get(attrName).toExpandedJsonString(entityDefDomain));
		}
	}
	
	protected void cloneToDefinitionEntityInDomain(HAPDefinitionEntityInDomain entityDefinitionInDomain) {
		entityDefinitionInDomain.m_entityType = this.m_entityType;
		for(String attrName : this.m_attributesSimple.keySet()) {
			entityDefinitionInDomain.m_attributesSimple.put(attrName, this.m_attributesSimple.get(attrName));
		}
		
		for(String attrName : this.m_attributeContainer.keySet()) {
			entityDefinitionInDomain.m_attributeContainer.put(attrName, this.m_attributeContainer.get(attrName));
		}
	}
}
