package com.nosliw.data.core.domain;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.domain.container.HAPContainerEntityDefinition;
import com.nosliw.data.core.domain.container.HAPElementContainerDefinition;

public abstract class HAPDefinitionEntityInDomain extends HAPSerializableImp implements HAPEntityOrReference{

	public final static String SIMPLEATTRIBUTE = "simpleAttributes"; 
	
	public final static String CONTAINERATTRIBUTE = "containerAttributes"; 
	
	//simple attribute by name
	private Map<String, HAPEmbededDefinitionWithId> m_attributesSimple;
	
	//container attribute by name
	private Map<String, HAPContainerEntityDefinition> m_attributeContainer;
	
	private String m_entityType;
	
	protected HAPDefinitionEntityInDomain() {
		this.m_attributesSimple = new LinkedHashMap<String, HAPEmbededDefinitionWithId>();
		this.m_attributeContainer = new LinkedHashMap<String, HAPContainerEntityDefinition>();
		this.m_entityType = HAPUtilityEntityDefinition.getEntityTypeFromEntityClass(this.getClass());
	}
	
	public HAPDefinitionEntityInDomain (String entityType) {
		this();
		this.m_entityType = entityType;
	}

	public Set<HAPIdEntityInDomain> getChildrenEntity(){
		Set<HAPIdEntityInDomain> out = new HashSet<HAPIdEntityInDomain>();
		getStandardChildrenEntity(out);
		return out;
	}
	
	public HAPIdEntityInDomain getChild(String childName) {	
		//search in simple attribute first
		HAPEmbededDefinitionWithId childEntity = this.m_attributesSimple.get(childName);
		if(childEntity!=null)  return childEntity.getEntityId();

		//then search in container attribute
		String[] containerSegs = HAPUtilityNamingConversion.parseLevel1(childName);
		HAPContainerEntityDefinition childContainer = this.m_attributeContainer.get(containerSegs[0]);
		if(childContainer!=null) {
			return getEmbeded(childContainer.getElementByName(containerSegs[1])).getEntityId();
		}
		
		return null;
	}

	public abstract HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain();

	protected void getStandardChildrenEntity(Set<HAPIdEntityInDomain> out){
		for(HAPEmbededDefinitionWithId simpleAttr : this.m_attributesSimple.values()) {
			out.add(simpleAttr.getEntityId());
		}
		
		for(HAPContainerEntityDefinition<HAPElementContainerDefinition> containerAttr : this.m_attributeContainer.values()) {
			for(HAPElementContainerDefinition eleInfo : containerAttr.getAllElements()) {
				out.add(getEmbeded(eleInfo).getEntityId());
			}
		}
	}
	
	private HAPEmbededDefinitionWithId getEmbeded(HAPElementContainerDefinition eleInfo) {		return (HAPEmbededDefinitionWithId)eleInfo.getEmbededElementEntity();	}
	
	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.ENTITY;    }

	public String getEntityType() {  return this.m_entityType;	}

	public HAPEmbededDefinitionWithId getSimpleAttribute(String attributeName) {		return this.m_attributesSimple.get(attributeName);	}

	public void setSimpleAttribute(String attributeName, HAPEmbededDefinitionWithId attrEntity) {
		if(attrEntity==null) {
			this.m_attributesSimple.remove(attributeName);
		}
		else {
			this.m_attributesSimple.put(attributeName, attrEntity);	
		}
	}
	
	public void removeSimpleAttribute(String attributeName) {
		this.m_attributesSimple.remove(attributeName);
	}
	
	public Map<String, HAPEmbededDefinitionWithId> getSimpleAttributes(){    return this.m_attributesSimple;     }
	
	public Map<String, HAPContainerEntityDefinition> getContainerAttributes(){    return this.m_attributeContainer;     }
	
//	public HAPEmbededDefinitionWithId getConatinerAttributeElementByName(String attributeName, String eleName) {
//		return getEmbeded(this.m_attributeContainer.get(attributeName).getElementByName(eleName));
//	}
	
	public void setContainerAttribute(String attributeName, HAPContainerEntityDefinition container) {
		this.m_attributeContainer.put(attributeName, container);
	}
	
//	public void addContainerElementAttribute(String attributeName, HAPInfoContainerElement eleInfo) {
//		HAPContainerEntity container = this.m_attributeContainer.get(attributeName);
//		if(container==null) {
//			String eleInfoType = eleInfo.getInfoType();
//			if(HAPConstantShared.ENTITYCONTAINER_TYPE_SET.equals(eleInfoType)) {
//				container = new HAPContainerEntitySet();
//			}
//			else if(HAPConstantShared.ENTITYCONTAINER_TYPE_LIST.equals(eleInfoType)) {
//				container = new HAPContainerEntityList();
//				
//			}
//			this.m_attributeContainer.put(attributeName, container);
//		}
//		container.addEntityElement(eleInfo);
//	}

	//normal json
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		for(String attrName : this.m_attributesSimple.keySet()) jsonMap.put(attrName, this.m_attributesSimple.get(attrName).toStringValue(HAPSerializationFormat.JSON));
		for(String attrName : this.m_attributeContainer.keySet())	jsonMap.put(attrName, this.m_attributeContainer.get(attrName).toStringValue(HAPSerializationFormat.JSON));
	}

	public String toExpandedJsonString(HAPDomainEntityDefinitionGlobal entityDefDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>(); 
		this.buildExpandedJsonMap(jsonMap, typeJsonMap, entityDefDomain);
		return HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap);
	}
	
	//expanded json. expand all referenced 
	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntityDefinitionGlobal entityDefDomain){
		this.buildJsonMap(jsonMap, typeJsonMap);
		for(String attrName : this.m_attributesSimple.keySet()) jsonMap.put(attrName, this.m_attributesSimple.get(attrName).toExpandedJsonString(entityDefDomain));
		for(String attrName : this.m_attributeContainer.keySet())	jsonMap.put(attrName, this.m_attributeContainer.get(attrName).toExpandedJsonString(entityDefDomain));
	}
	
	protected void cloneToDefinitionEntityInDomain(HAPDefinitionEntityInDomain entityDefinitionInDomain) {
		entityDefinitionInDomain.m_entityType = this.m_entityType;
		for(String attrName : this.m_attributesSimple.keySet())		entityDefinitionInDomain.m_attributesSimple.put(attrName, this.m_attributesSimple.get(attrName));
		for(String attrName : this.m_attributeContainer.keySet()) 	entityDefinitionInDomain.m_attributeContainer.put(attrName, this.m_attributeContainer.get(attrName));
	}
}
