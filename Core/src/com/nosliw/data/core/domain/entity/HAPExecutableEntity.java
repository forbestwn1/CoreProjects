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
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public abstract class HAPExecutableEntity extends HAPExecutableImp implements HAPExpandable, HAPEntity{

	@HAPAttribute
	public static final String ENTITYTYPE = "entityType";
	@HAPAttribute
	public static final String ATTRIBUTE = "attribute";
	@HAPAttribute
	public static final String PARENT = "parent";

	private String m_entityType;
	
	private List<HAPAttributeEntityExecutable> m_attributes;

	private HAPExecutableEntity m_parent;
	
	private HAPIdEntityInDomain m_definitionEntityId;
	
	public HAPExecutableEntity() {
		this.m_attributes = new ArrayList<HAPAttributeEntityExecutable>();
	} 

	public HAPExecutableEntity(String entityType) {
		this.m_entityType = entityType;
		this.m_attributes = new ArrayList<HAPAttributeEntityExecutable>();
	} 
	
	public void setDefinitionEntityId(HAPIdEntityInDomain defEntityId) {    this.m_definitionEntityId = defEntityId;      }
	public HAPIdEntityInDomain getDefinitionEntityId() {    return this.m_definitionEntityId;     }
	
	public String getEntityType() {    return this.m_entityType;   }
	public void setEntityType(String entityType) {    this.m_entityType = entityType;     }

	public HAPExecutableEntity getParent() {    return this.m_parent;     }
	public void setParent(HAPExecutableEntity parent) {    this.m_parent = parent;      }
	
	
	public List<HAPAttributeEntityExecutable> getAttributes(){    return this.m_attributes;     }
	
	public HAPAttributeEntityExecutable getAttribute(String attrName) {
		for(HAPAttributeEntityExecutable attr : this.m_attributes) {
			if(attrName.equals(attr.getName()))  return attr;
		}
		return null;
	}

	public HAPEmbededExecutable getAttributeEmbeded(String attrName) {
		HAPAttributeEntityExecutable attr = this.getAttribute(attrName);
		return attr==null?null:attr.getValue(); 
	}

	public Object getAttributeValue(String attrName) {
		Object out = null;
		HAPEmbededExecutable embeded = this.getAttributeEmbeded(attrName);
		if(embeded!=null) {
			out = embeded.getValue();
		}
		return out;
	}
	
	public void setAttribute(HAPAttributeEntityExecutable attrObj) {
		attrObj.setParentEntity(this);
		this.m_attributes.add(attrObj);    
	}
	
	public void setAttributeValueObject(String attributeName, Object value) {    setAttribute(attributeName, new HAPEmbededExecutable(value), new HAPInfoValueType());   }
	
	public void setAttribute(String attributeName, HAPEmbededExecutable embededEntity, HAPInfoValueType valueTypeInfo) {	this.setAttribute(new HAPAttributeEntityExecutable(attributeName, embededEntity, valueTypeInfo));	}
	public void setAttributeSimple(String attributeName, HAPEmbededExecutable embededEntity, String valueType) {  setAttribute(attributeName, embededEntity, new HAPInfoValueType(valueType, false)); }
	public void setAttributeComplex(String attributeName, HAPEmbededExecutable embededEntity, String valueType) {  setAttribute(attributeName, embededEntity, new HAPInfoValueType(valueType, true)); }

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

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		for(HAPAttributeEntityExecutable attribute : this.m_attributes) {
			HAPEmbededExecutable embeded = attribute.getValue();
			Object valueObj = embeded.getValue();
			if(valueObj instanceof HAPExecutable) {
				dependency.addAll(((HAPExecutable)valueObj).getResourceDependency(runtimeInfo, resourceManager));
			}
		
			for(HAPInfoAdapter adapterInfo : embeded.getAdapters()) {
				Object adapterObj = adapterInfo.getValue();
				if(adapterObj instanceof HAPExecutable) {
					dependency.addAll(((HAPExecutable)adapterObj).getResourceDependency(runtimeInfo, resourceManager));
				}
			}
		}
	}
}
