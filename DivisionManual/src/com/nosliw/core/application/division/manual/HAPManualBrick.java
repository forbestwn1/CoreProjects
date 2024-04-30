package com.nosliw.core.application.division.manual;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPUtilityNosliw;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.data.core.domain.entity.HAPEntity;

public abstract class HAPManualBrick extends HAPSerializableImp implements HAPEntityOrReference, HAPEntity{

	public final static String ATTRIBUTE = "attribute"; 
	
	final private static String ATTR_IDINDEX = HAPUtilityNosliw.buildNosliwFullName("idIndex"); 

	//all attributes
	private List<HAPManualAttribute> m_attributes;
	
	private HAPIdBrickType m_brickTypeId;
	
	protected HAPManualBrick (HAPIdBrickType brickTypeId) {
		this.m_attributes = new ArrayList<HAPManualAttribute>();
		this.m_brickTypeId = brickTypeId;
	}

	@Override
	public String getEntityOrReferenceType() {return null;}

	
	public HAPIdBrickType getBrickTypeId() {  return this.m_brickTypeId;	}

	public void setAttribute(HAPManualAttribute attribute) {
		for(int i=0; i<this.m_attributes.size(); i++) {
			if(this.m_attributes.get(i).getName().equals(attribute.getName())) {
				this.m_attributes.remove(i);
				break;
			}
		}
		this.m_attributes.add(attribute);    
	}

	public List<HAPManualAttribute> getAllAttributes(){    return this.m_attributes;    }
	public List<HAPManualAttribute> getPublicAttributes(){
		List<HAPManualAttribute> out = new ArrayList<HAPManualAttribute>();
		for(HAPManualAttribute attr : this.getAllAttributes()) {
			if(HAPUtilityNosliw.getNosliwCoreName(attr.getName())==null) {
				out.add(attr);
			}
		}
		return out;    
	}
	
	public HAPManualAttribute getAttribute(String attrName) {
		HAPManualAttribute out = null;
		for(HAPManualAttribute attr : this.m_attributes) {
			if(attr.getName().equals(attrName)) {
				return attr;
			}
		}
		return out;
	}
	
	public void setAttributeWithValueValue(String attributeName, Object attrValue) {	this.setAttribute(new HAPManualAttribute(attributeName, new HAPManualWrapperValueValue(attrValue)));	}
	public void setAttributeWithValueBrick(String attributeName, HAPManualBrick brick) {
		 this.setAttribute(new HAPManualAttribute(attributeName, new HAPManualWrapperValueBrick(brick)));	
	}
	
	public Object getAttributeValueWithValue(String attributeName){
		HAPManualAttribute attr = this.getAttribute(attributeName);
		if(attr!=null) {
			return ((HAPManualWrapperValueValue)attr.getValueInfo()).getValue();
		}
		return null;
	}
	public Object getAttributeValueWithValue(String attrName, Object defaultValue) {
		HAPManualAttribute att = this.getAttribute(attrName);
		if(att==null) {
			this.setAttributeWithValueValue(attrName, defaultValue);
			att = this.getAttribute(attrName);
		}
		return ((HAPManualWrapperValueValue)att.getValueInfo()).getValue();
	}
	
	public HAPManualBrick getAttributeValueWithBrick(String attributeName) {
		HAPManualAttribute attr = this.getAttribute(attributeName);
		if(attr!=null) {
			return ((HAPManualWrapperValueBrick)attr.getValueInfo()).getBrick();
		}
		return null;
	}

	protected String generateId() {
		int idIndex = (Integer)this.getAttributeValueWithValue(ATTR_IDINDEX, Integer.valueOf(0));
		idIndex++;
		this.setAttributeWithValueValue(ATTR_IDINDEX, idIndex);
		return "generatedId_"+ idIndex;
	}
	
	//normal json
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		Map<String, String> attrMap = new LinkedHashMap<String, String>();
		for(HAPManualAttribute attribute : this.getAllAttributes()) {
			attrMap.put(attribute.getName(),  attribute.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildMapJson(attrMap));
	}


	
	
	
	
/*	
	
	
	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.ENTITY;    }

	public void setEntityType(String entityType) {    this.m_entityTypeId = entityType;     }

	public HAPEmbededDefinition getAttributeEmbeded(String attrName) {
		HAPManualAttribute att = this.getAttribute(attrName);
		if(att!=null) {
			return att.getValue();
		} else {
			return null;
		}
	}
	
	public Object getAttributeValue1(String attrName) {
		Object out = null;
		HAPEmbededDefinition embeded = this.getAttributeEmbeded(attrName);
		if(embeded!=null) {
			out = embeded.getValue();
		}
		return out;
	}

	public HAPIdEntityInDomain getAttributeValueEntityId(String attrName) {    return (HAPIdEntityInDomain)this.getAttributeValue(attrName);      }
	public HAPManualBrick getAttributeValueEntity(String attrName, HAPContextParser parserContext) {
		HAPManualBrick out = null;
		HAPIdEntityInDomain attrEntityId = (HAPIdEntityInDomain)this.getAttributeValue(attrName);
		if(attrEntityId!=null) {
			out = parserContext.getGlobalDomain().getEntityDefinition(attrEntityId);
		}
		return out;
	}
	
	public void setAttribute(String attributeName, HAPEmbededDefinition embededEntity, HAPInfoBrickType valueTypeInfo, Boolean autoProcess) {
		if(embededEntity!=null) {
			Object value = embededEntity.getValue();
			HAPManualAttribute attribute = new HAPManualAttribute(attributeName, embededEntity, valueTypeInfo);	
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

	public void setAttribute(String attributeName, HAPEmbededDefinition embededEntity, HAPInfoBrickType valueTypeInfo) {
		this.setAttribute(attributeName, embededEntity, valueTypeInfo, null);
	}

	public void setAttributeValue(String attributeName, Object attrValue, HAPInfoBrickType valueTypeInfo) {	this.setAttribute(attributeName, new HAPEmbededDefinition(attrValue), valueTypeInfo);	}
	public void setAttributeValueObject1(String attributeName, Object attrValue) {	this.setAttribute(attributeName, new HAPEmbededDefinition(attrValue), new HAPInfoBrickType());	}
	public void setAttributeValueSimple(String attributeName, HAPIdEntityInDomain attrEntityIdInDomain) {setAttributeValue(attributeName, attrEntityIdInDomain, new HAPInfoBrickType(attrEntityIdInDomain.getEntityType(), false));}
	public void setAttributeValueComplex(String attributeName, HAPIdEntityInDomain attrEntityIdInDomain) {setAttributeValue(attributeName, attrEntityIdInDomain, new HAPInfoBrickType(attrEntityIdInDomain.getEntityType(), true));}
	
	
	public void setAttributeObject(String attributeName, HAPEmbededDefinition embededEntity) {setAttribute(attributeName, embededEntity, new HAPInfoBrickType());}
	public void setAttributeSimple(String attributeName, HAPEmbededDefinition embededEntity) {setAttribute(attributeName, embededEntity, new HAPInfoBrickType(((HAPIdEntityInDomain)embededEntity.getValue()).getEntityType(), false));}
	public void setAttributeComplex(String attributeName, HAPEmbededDefinition embededEntity) {setAttribute(attributeName, embededEntity, new HAPInfoBrickType(((HAPIdEntityInDomain)embededEntity.getValue()).getEntityType(), true));}

	public HAPIdEntityInDomain getChild(String childName) {	
		return (HAPIdEntityInDomain)this.getAttribute(childName).getValue().getValue();
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
	
	public abstract HAPManualBrick cloneEntityDefinitionInDomain();

	protected void cloneToDefinitionEntityInDomain(HAPManualBrick entityDefinitionInDomain) {
		entityDefinitionInDomain.m_entityTypeId = this.m_entityTypeId;
		entityDefinitionInDomain.m_attributes = new ArrayList<HAPManualAttribute>();
		for(HAPManualAttribute attribute : this.getAttributes()) {
			entityDefinitionInDomain.setAttribute((HAPManualAttribute)attribute.cloneEntityAttribute());
		}
	}
*/	
}
