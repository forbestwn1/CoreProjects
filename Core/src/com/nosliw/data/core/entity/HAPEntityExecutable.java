package com.nosliw.data.core.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPEntityExecutable extends HAPExecutableImp{

	@HAPAttribute
	public final static String ATTRIBUTE = "attribute"; 

	private HAPIdEntityType m_entityTypeId;
	
	//all attributes
	private List<HAPAttributeExecutable> m_attributes;
	
	public HAPEntityExecutable() {
		this.m_attributes = new ArrayList<HAPAttributeExecutable>();
	}
	
	public List<HAPAttributeExecutable> getAttributes(){     return this.m_attributes;      }
	
	public HAPIdEntityType getEntityTypeId() {    return this.m_entityTypeId;     }
	public void setEntityTypeId(HAPIdEntityType entityTypeId) {    this.m_entityTypeId = entityTypeId;     }
	
	public void setAttribute(HAPAttributeExecutable attribute) {
		for(int i=0; i<this.m_attributes.size(); i++) {
			if(this.m_attributes.get(i).getName().equals(attribute.getName())) {
				this.m_attributes.remove(i);
				break;
			}
		}
		this.m_attributes.add(attribute);    
	}
	
	public void setAttributeValue(String attributeName, Object attrValue) {	this.setAttribute(new HAPAttributeExecutable(attributeName, new HAPInfoAttributeValueValue(attrValue)));	}

	public HAPAttributeExecutable getAttribute(String attrName) {
		for(HAPAttributeExecutable attr : this.m_attributes) {
			if(attrName.equals(attr.getName())) {
				return attr;
			}
		}
		return null;
	}

	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		Map<String, String> attrJsonMap = new LinkedHashMap<String, String>();
		for(HAPAttributeExecutable attr : this.m_attributes) {
			attrJsonMap.put(attr.getName(), attr.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildMapJson(attrJsonMap));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		this.buildJsonMap(jsonMap, typeJsonMap);
		Map<String, String> attrJsonMap = new LinkedHashMap<String, String>();
		for(HAPAttributeExecutable attr : this.m_attributes) {
			attrJsonMap.put(attr.getName(), attr.toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildMapJson(attrJsonMap));
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		for(HAPAttributeExecutable attr : this.m_attributes) {
			dependency.addAll(attr.getResourceDependency(runtimeInfo, resourceManager));
		}
	}
}
