package com.nosliw.data.core.domain.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.container.HAPContainerEntityDefinition;
import com.nosliw.data.core.domain.container.HAPElementContainerDefinition;

@HAPEntityWithAttribute
public abstract class HAPDefinitionEntityInDomain extends HAPSerializableImp implements HAPEntityOrReference{

	@HAPAttribute
	public final static String ATTRIBUTE = "attribute"; 
	
	//all attributes, two types, simple and container
	private List<HAPAttributeEntityDefinition> m_attributes;
	
	private String m_entityType;
	
	protected HAPDefinitionEntityInDomain() {
		this.m_attributes = new ArrayList<HAPAttributeEntityDefinition>();
	}
	
	public HAPDefinitionEntityInDomain (String entityType) {
		this();
		this.m_entityType = entityType;
	}

	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.ENTITY;    }

	public String getEntityType() {  return this.m_entityType;	}

	public void setEntityType(String entityType) {    this.m_entityType = entityType;     }
	
	public List<HAPAttributeEntityDefinition> getAttributes(){    return this.m_attributes;    }
	
	public HAPAttributeEntityDefinition getAttribute(String attrName) {
		HAPAttributeEntityDefinition out = null;
		for(HAPAttributeEntityDefinition attr : this.m_attributes) {
			if(attr.getName().equals(attrName))  return attr;
		}
		return out;
	}
	
	public HAPAttributeEntityDefinitionNormal getNormalAttribute(String attrName) {   return (HAPAttributeEntityDefinitionNormal)this.getAttribute(attrName);    }
	
	public HAPEmbededDefinition getNormalAttributeEmbeded(String attrName) {
		HAPAttributeEntityDefinitionNormal att = this.getNormalAttribute(attrName);
		if(att!=null)  return att.getValue();
		else return null;
	}
	
	public Object getNormalAttributeValue(String attrName) {
		Object out = null;
		HAPEmbededDefinition embeded = this.getNormalAttributeEmbeded(attrName);
		if(embeded!=null) {
			out = embeded.getValue();
		}
		return out;
	}

	public Object getNormalAttributeValue(String attrName, Object defaultValue) {
		HAPAttributeEntityDefinitionNormal att = this.getNormalAttribute(attrName);
		if(att==null) {
			this.setNormalAttributeSimple(attrName, new HAPEmbededDefinition(defaultValue), null);
			att = this.getNormalAttribute(attrName);
		}
		return att.getValue().getValue();
		
	}	
	
	public HAPAttributeEntityDefinitionContainer getContainerAttribute(String attrName) {    return (HAPAttributeEntityDefinitionContainer)this.getAttribute(attrName);    }
	
	public void setContainerAttribute(String attributeName, HAPContainerEntityDefinition container, HAPInfoValueType valueTypeInfo) {
		if(container!=null)		this.setAttribute(new HAPAttributeEntityDefinitionContainer(attributeName, container, valueTypeInfo));    
	}
	public void setContainerAttributeSimple(String attributeName, HAPContainerEntityDefinition container, String valueType) {	this.setContainerAttribute(attributeName, container, new HAPInfoValueType(valueType, false));  	}
	public void setContainerAttributeComplex(String attributeName, HAPContainerEntityDefinition container, String valueType) {	this.setContainerAttribute(attributeName, container, new HAPInfoValueType(valueType, true));  }

	public void setNormalAttributeValue(String attributeName, Object attrValue, HAPInfoValueType valueTypeInfo) {	this.setNormalAttribute(attributeName, new HAPEmbededDefinition(attrValue), valueTypeInfo);	}
	public void setNormalAttributeValueObject(String attributeName, Object attrValue) {	this.setNormalAttribute(attributeName, new HAPEmbededDefinition(attrValue), new HAPInfoValueType());	}
	public void setNormalAttributeValueSimple(String attributeName, HAPIdEntityInDomain attrEntityIdInDomain) {setNormalAttributeValue(attributeName, attrEntityIdInDomain, new HAPInfoValueType(attrEntityIdInDomain.getEntityType(), false));}
	public void setNormalAttributeValueComplex(String attributeName, HAPIdEntityInDomain attrEntityIdInDomain) {setNormalAttributeValue(attributeName, attrEntityIdInDomain, new HAPInfoValueType(attrEntityIdInDomain.getEntityType(), true));}
	
	public void setNormalAttribute(String attributeName, HAPEmbededDefinition embededEntity, HAPInfoValueType valueTypeInfo) {
		if(embededEntity!=null) {
			this.setAttribute(new HAPAttributeEntityDefinitionNormal(attributeName, embededEntity, valueTypeInfo));
		}
	}
	
	public void setNormalAttributeObject(String attributeName, HAPEmbededDefinition embededEntity) {setNormalAttribute(attributeName, embededEntity, new HAPInfoValueType());}
	public void setNormalAttributeSimple(String attributeName, HAPEmbededDefinition embededEntity, String valueType) {setNormalAttribute(attributeName, embededEntity, new HAPInfoValueType(valueType, false));}
	public void setNormalAttributeComplex(String attributeName, HAPEmbededDefinition embededEntity, String valueType) {setNormalAttribute(attributeName, embededEntity, new HAPInfoValueType(valueType, true));}

	public void setAttribute(HAPAttributeEntityDefinition attribute) {    this.m_attributes.add(attribute);    }

	//normal json
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		Map<String, String> attrMap = new LinkedHashMap<String, String>();
		for(HAPAttributeEntity attribute : this.getAttributes()) {
			attrMap.put(attribute.getName(),  attribute.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildMapJson(attrMap));
	}

	public String toExpandedJsonString(HAPDomainEntityDefinitionGlobal entityDefDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>(); 

		List<String> attrArray = new ArrayList<String>();
		for(HAPAttributeEntity attribute : this.getAttributes()) {
			attrArray.add(attribute.toExpandedJsonString(entityDefDomain));
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildArrayJson(attrArray.toArray(new String[0])));
		return HAPUtilityJson.buildMapJson(jsonMap, typeJsonMap);
	}
	
	public abstract HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain();

	protected void cloneToDefinitionEntityInDomain(HAPDefinitionEntityInDomain entityDefinitionInDomain) {
		entityDefinitionInDomain.m_entityType = this.m_entityType;
		entityDefinitionInDomain.m_attributes = new ArrayList<HAPAttributeEntityDefinition>();
		for(HAPAttributeEntityDefinition attribute : this.getAttributes()) {
			entityDefinitionInDomain.setAttribute((HAPAttributeEntityDefinition)attribute.cloneEntityAttribute());
		}
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
	
	
//	public HAPEmbededDefinitionWithId getConatinerAttributeElementByName(String attributeName, String eleName) {
//		return getEmbeded(this.m_attributeContainer.get(attributeName).getElementByName(eleName));
//	}
	
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

}
