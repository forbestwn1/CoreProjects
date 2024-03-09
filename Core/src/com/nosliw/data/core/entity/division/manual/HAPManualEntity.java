package com.nosliw.data.core.entity.division.manual;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNosliw;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPAttributeEntity;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.domain.entity.HAPEntity;
import com.nosliw.data.core.entity.HAPIdEntityType;
import com.nosliw.data.core.entity.HAPInfoEntityType;

@HAPEntityWithAttribute
public abstract class HAPManualEntity extends HAPSerializableImp implements HAPEntityOrReference, HAPEntity{

	@HAPAttribute
	public final static String ATTRIBUTE = "attribute"; 
	
	final private static String ATTR_IDINDEX = HAPUtilityNosliw.buildNosliwFullName("idIndex"); 

	//all attributes
	private List<HAPManualAttribute> m_attributes;
	
	private HAPIdEntityType m_entityTypeId;
	
	protected HAPManualEntity (HAPIdEntityType entityTypeId) {
		this.m_attributes = new ArrayList<HAPManualAttribute>();
		this.m_entityTypeId = entityTypeId;
	}

	public HAPIdEntityType getEntityTypeId() {  return this.m_entityTypeId;	}

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
	
	public void setAttributeValue(String attributeName, Object attrValue) {	this.setAttribute(new HAPManualAttribute(attributeName, new HAPManualInfoAttributeValueValue(attrValue)));	}
	public Object getAttributeValue(String attributeName){	return ((HAPManualInfoAttributeValueValue)this.getAttribute(attributeName).getValueInfo()).getValue();	}
	public Object getAttributeValue(String attrName, Object defaultValue) {
		HAPManualAttribute att = this.getAttribute(attrName);
		if(att==null) {
			this.setAttributeValue(attrName, defaultValue);
			att = this.getAttribute(attrName);
		}
		return ((HAPManualInfoAttributeValueValue)att.getValueInfo()).getValue();
	}	
	
	public void setAttributeEntity(String attributeName, HAPManualEntity entity) {
		 this.setAttribute(new HAPManualAttribute(attributeName, new HAPManualInfoAttributeValueEntity(entity)));	
	}
	public HAPManualEntity getAttributeEntity(String attributeName) {
		return ((HAPManualInfoAttributeValueEntity)this.getAttribute(attributeName).getValueInfo()).getEntity();
	}

	protected String generateId() {
		int idIndex = (Integer)this.getAttributeValue(ATTR_IDINDEX, Integer.valueOf(0));
		idIndex++;
		this.setAttributeValue(ATTR_IDINDEX, idIndex);
		return "generatedId_"+ idIndex;
	}
	
	public HAPManualAttribute getDescendantAttribute(HAPPath path) {
		HAPManualAttribute out = null;
		for(int i=0; i<path.getLength(); i++) {
			String attribute = path.getPathSegments()[i];
			if(i==0) {
				out = this.getAttribute(attribute);
			} else {
				HAPManualInfoAttributeValue attrValueInfo = out.getValueInfo();
				if(attrValueInfo instanceof HAPManualWithEntity) {
					((HAPManualWithEntity)attrValueInfo).getEntity().getAttribute(attribute);
				}
				else if(attrValueInfo.getValueType().equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID)) {
					throw new RuntimeException();
				}
			}
		}
		return out;
	}

	public HAPManualEntity getDescendantEntity(HAPPath path) {
		HAPManualEntity out = null;
		if(path==null||path.isEmpty()) {
			out = this;
		} else {
			HAPManualInfoAttributeValue attrValueInfo = this.getDescendantAttribute(path).getValueInfo();
			if(attrValueInfo instanceof HAPManualWithEntity) {
				out = ((HAPManualWithEntity)attrValueInfo).getEntity();
			}
			else {
				throw new RuntimeException();
			}
		}
		return out;
	}

	
	
	
	
	
	
	public HAPManualInfoAttributeValue getDescendantValueInfo(HAPPath path) {
		
		
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
	public HAPManualEntity getAttributeValueEntity(String attrName, HAPContextParser parserContext) {
		HAPManualEntity out = null;
		HAPIdEntityInDomain attrEntityId = (HAPIdEntityInDomain)this.getAttributeValue(attrName);
		if(attrEntityId!=null) {
			out = parserContext.getGlobalDomain().getEntityDefinition(attrEntityId);
		}
		return out;
	}
	
	public void setAttribute(String attributeName, HAPEmbededDefinition embededEntity, HAPInfoEntityType valueTypeInfo, Boolean autoProcess) {
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

	public void setAttribute(String attributeName, HAPEmbededDefinition embededEntity, HAPInfoEntityType valueTypeInfo) {
		this.setAttribute(attributeName, embededEntity, valueTypeInfo, null);
	}

	public void setAttributeValue(String attributeName, Object attrValue, HAPInfoEntityType valueTypeInfo) {	this.setAttribute(attributeName, new HAPEmbededDefinition(attrValue), valueTypeInfo);	}
	public void setAttributeValueObject1(String attributeName, Object attrValue) {	this.setAttribute(attributeName, new HAPEmbededDefinition(attrValue), new HAPInfoEntityType());	}
	public void setAttributeValueSimple(String attributeName, HAPIdEntityInDomain attrEntityIdInDomain) {setAttributeValue(attributeName, attrEntityIdInDomain, new HAPInfoEntityType(attrEntityIdInDomain.getEntityType(), false));}
	public void setAttributeValueComplex(String attributeName, HAPIdEntityInDomain attrEntityIdInDomain) {setAttributeValue(attributeName, attrEntityIdInDomain, new HAPInfoEntityType(attrEntityIdInDomain.getEntityType(), true));}
	
	
	public void setAttributeObject(String attributeName, HAPEmbededDefinition embededEntity) {setAttribute(attributeName, embededEntity, new HAPInfoEntityType());}
	public void setAttributeSimple(String attributeName, HAPEmbededDefinition embededEntity) {setAttribute(attributeName, embededEntity, new HAPInfoEntityType(((HAPIdEntityInDomain)embededEntity.getValue()).getEntityType(), false));}
	public void setAttributeComplex(String attributeName, HAPEmbededDefinition embededEntity) {setAttribute(attributeName, embededEntity, new HAPInfoEntityType(((HAPIdEntityInDomain)embededEntity.getValue()).getEntityType(), true));}

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
	
	public abstract HAPManualEntity cloneEntityDefinitionInDomain();

	protected void cloneToDefinitionEntityInDomain(HAPManualEntity entityDefinitionInDomain) {
		entityDefinitionInDomain.m_entityTypeId = this.m_entityTypeId;
		entityDefinitionInDomain.m_attributes = new ArrayList<HAPManualAttribute>();
		for(HAPManualAttribute attribute : this.getAttributes()) {
			entityDefinitionInDomain.setAttribute((HAPManualAttribute)attribute.cloneEntityAttribute());
		}
	}
}
