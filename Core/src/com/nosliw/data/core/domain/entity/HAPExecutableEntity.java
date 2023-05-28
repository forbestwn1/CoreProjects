package com.nosliw.data.core.domain.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPExpandable;
import com.nosliw.data.core.domain.container.HAPContainerEntityExecutable;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public abstract class HAPExecutableEntity extends HAPExecutableImp implements HAPExpandable{

	@HAPAttribute
	public static final String ENTITYTYPE = "entityType";
	@HAPAttribute
	public static final String ATTRIBUTE = "attribute";

	private String m_entityType;
	
	private List<HAPAttributeEntityExecutable> m_attributes;

	public HAPExecutableEntity() {
		this.m_attributes = new ArrayList<HAPAttributeEntityExecutable>();
	} 

	public HAPExecutableEntity(String entityType) {
		this.m_entityType = entityType;
		this.m_attributes = new ArrayList<HAPAttributeEntityExecutable>();
	} 
	
	public String getEntityType() {    return this.m_entityType;   }

	public void setEntityType(String entityType) {    this.m_entityType = entityType;     }
	
	public List<HAPAttributeEntityExecutable> getAttributes(){    return this.m_attributes;     }
	
	public HAPAttributeEntityExecutable getAttribute(String attrName) {
		for(HAPAttributeEntityExecutable attr : this.m_attributes) {
			if(attrName.equals(attr.getName()))  return attr;
		}
		return null;
	}

	public HAPEmbededExecutable getNormalAttributeEmbeded(String attrName) { return (HAPEmbededExecutable)this.getAttribute(attrName).getValue(); }

	public Object getNormalAttributeValue(String attrName) {
		Object out = null;
		HAPEmbededExecutable embeded = this.getNormalAttributeEmbeded(attrName);
		if(embeded!=null) {
			out = embeded.getValue();
		}
		return out;
	}
	
	public void setAttribute(HAPAttributeEntityExecutable attrObj) {    this.m_attributes.add(attrObj);    }
	
	public void setNormalAttributeValueObject(String attributeName, Object value) {    setNormalAttribute(attributeName, new HAPEmbededExecutable(value), new HAPInfoValueType());   }
	
	public void setNormalAttribute(String attributeName, HAPEmbededExecutable embededEntity, HAPInfoValueType valueTypeInfo) {	this.setAttribute(new HAPAttributeEntityExecutableNormal(attributeName, embededEntity, valueTypeInfo));	}
//	public void setNormalAttributeObject(String attributeName, HAPEmbededExecutable embededEntity) {  setNormalAttribute(attributeName, embededEntity, new HAPInfoValueType()); }
	public void setNormalAttributeSimple(String attributeName, HAPEmbededExecutable embededEntity, String valueType) {  setNormalAttribute(attributeName, embededEntity, new HAPInfoValueType(valueType, false)); }
	public void setNormalAttributeComplex(String attributeName, HAPEmbededExecutable embededEntity, String valueType) {  setNormalAttribute(attributeName, embededEntity, new HAPInfoValueType(valueType, true)); }

	public void setContainerAttribute(String attributeName, HAPContainerEntityExecutable container, HAPInfoValueType valueTypeInfo) {
		if(container!=null)		this.setAttribute(new HAPAttributeEntityExecutableContainer(attributeName, container, valueTypeInfo));
	}
	public void setContainerAttributeObject(String attributeName, HAPContainerEntityExecutable container) { setContainerAttribute(attributeName, container, new HAPInfoValueType());  }
	public void setContainerAttributeSimple(String attributeName, HAPContainerEntityExecutable container, String valueType) { setContainerAttribute(attributeName, container, new HAPInfoValueType(valueType, false));  }
	public void setContainerAttributeComplex(String attributeName, HAPContainerEntityExecutable container, String valueType) { setContainerAttribute(attributeName, container, new HAPInfoValueType(valueType, true));  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		buildCommonJsonMap(jsonMap, typeJsonMap);

		List<String> attrArray = new ArrayList<String>();
		for(HAPAttributeEntityExecutable attribute : this.m_attributes) {
			attrArray.add(attribute.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildArrayJson(attrArray.toArray(new String[0])));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		buildCommonJsonMap(jsonMap, typeJsonMap);

		List<String> attrArray = new ArrayList<String>();
		for(HAPAttributeEntityExecutable attribute : this.m_attributes) {
			attrArray.add(attribute.toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildArrayJson(attrArray.toArray(new String[0])));
	}

	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		
		this.buildExpandedJsonMap(jsonMap, typeJsonMap, entityDomain);
		return HAPUtilityJson.buildMapJson(jsonMap, typeJsonMap);
	}

	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntity entityDomain) {
		buildCommonJsonMap(jsonMap, typeJsonMap);

		List<String> attrArray = new ArrayList<String>();
		for(HAPAttributeEntityExecutable attribute : this.m_attributes) {
			attrArray.add(attribute.toExpandedJsonString(entityDomain));
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildArrayJson(attrArray.toArray(new String[0])));
	}

	protected void buildCommonJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {	
		jsonMap.put(ENTITYTYPE, this.m_entityType);
	}
}
