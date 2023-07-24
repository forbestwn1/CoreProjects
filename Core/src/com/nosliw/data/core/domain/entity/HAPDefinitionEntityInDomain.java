package com.nosliw.data.core.domain.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

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
	
	public HAPEmbededDefinition getAttributeEmbeded(String attrName) {
		HAPAttributeEntityDefinition att = this.getAttribute(attrName);
		if(att!=null)  return att.getValue();
		else return null;
	}
	
	public Object getAttributeValue(String attrName) {
		Object out = null;
		HAPEmbededDefinition embeded = this.getAttributeEmbeded(attrName);
		if(embeded!=null) {
			out = embeded.getValue();
		}
		return out;
	}

	public Object getAttributeValue(String attrName, Object defaultValue) {
		HAPAttributeEntityDefinition att = this.getAttribute(attrName);
		if(att==null) {
			this.setAttributeSimple(attrName, new HAPEmbededDefinition(defaultValue), null);
			att = this.getAttribute(attrName);
		}
		return att.getValue().getValue();
		
	}	
	
	public void setAttribute(String attributeName, HAPEmbededDefinition embededEntity, HAPInfoValueType valueTypeInfo, Boolean autoProcess) {
		if(embededEntity!=null) {
			Object value = embededEntity.getValue();
			HAPAttributeEntityDefinition attribute = new HAPAttributeEntityDefinition(attributeName, embededEntity, valueTypeInfo);	
			if(!(value instanceof HAPIdEntityInDomain)) {
				//for not entity attribute, then not autoprocess anyway
				attribute.setAttributeAutoProcess(false);
			}
			else if(autoProcess!=null) {
				attribute.setAttributeAutoProcess(autoProcess);
			}
			this.setAttribute(attribute);
		}
	}

	public void setAttribute(String attributeName, HAPEmbededDefinition embededEntity, HAPInfoValueType valueTypeInfo) {
		this.setAttribute(attributeName, embededEntity, valueTypeInfo, null);
	}

	public void setAttributeValue(String attributeName, Object attrValue, HAPInfoValueType valueTypeInfo) {	this.setAttribute(attributeName, new HAPEmbededDefinition(attrValue), valueTypeInfo);	}
	public void setAttributeValueObject(String attributeName, Object attrValue) {	this.setAttribute(attributeName, new HAPEmbededDefinition(attrValue), new HAPInfoValueType());	}
	public void setAttributeValueSimple(String attributeName, HAPIdEntityInDomain attrEntityIdInDomain) {setAttributeValue(attributeName, attrEntityIdInDomain, new HAPInfoValueType(attrEntityIdInDomain.getEntityType(), false));}
	public void setAttributeValueComplex(String attributeName, HAPIdEntityInDomain attrEntityIdInDomain) {setAttributeValue(attributeName, attrEntityIdInDomain, new HAPInfoValueType(attrEntityIdInDomain.getEntityType(), true));}
	
	
	public void setAttributeObject(String attributeName, HAPEmbededDefinition embededEntity) {setAttribute(attributeName, embededEntity, new HAPInfoValueType());}
	public void setAttributeSimple(String attributeName, HAPEmbededDefinition embededEntity, String valueType) {setAttribute(attributeName, embededEntity, new HAPInfoValueType(valueType, false));}
	public void setAttributeComplex(String attributeName, HAPEmbededDefinition embededEntity, String valueType) {setAttribute(attributeName, embededEntity, new HAPInfoValueType(valueType, true));}

	public void setAttribute(HAPAttributeEntityDefinition attribute) {    this.m_attributes.add(attribute);    }

	public HAPIdEntityInDomain getChild(String childName) {	
		return (HAPIdEntityInDomain)this.getAttribute(childName).getValue().getValue();
	}
	
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
}
