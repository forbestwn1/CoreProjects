package com.nosliw.core.application;

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
public class HAPBrick extends HAPExecutableImp{

	@HAPAttribute
	public final static String ATTRIBUTE = "attribute"; 

	@HAPAttribute
	public final static String BRICKTYPEID = "brickTypeId"; 

	private HAPIdBrickType m_brickTypeId;
	
	//all attributes
	private List<HAPAttributeInBrick> m_attributes;
	
	public HAPBrick() {
		this.m_attributes = new ArrayList<HAPAttributeInBrick>();
	}
	
	public List<HAPAttributeInBrick> getAttributes(){     return this.m_attributes;      }
	
	public HAPIdBrickType getBrickTypeId() {    return this.m_brickTypeId;     }
	public void setBrickTypeId(HAPIdBrickType brickTypeId) {    this.m_brickTypeId = brickTypeId;     }
	
	public void setAttribute(HAPAttributeInBrick attribute) {
		for(int i=0; i<this.m_attributes.size(); i++) {
			if(this.m_attributes.get(i).getName().equals(attribute.getName())) {
				this.m_attributes.remove(i);
				break;
			}
		}
		this.m_attributes.add(attribute);    
	}
	
	public void setAttributeValue(String attributeName, Object attrValue) {	this.setAttribute(new HAPAttributeInBrick(attributeName, new HAPWrapperValueInAttributeValue(attrValue)));	}

	public HAPAttributeInBrick getAttribute(String attrName) {
		for(HAPAttributeInBrick attr : this.m_attributes) {
			if(attrName.equals(attr.getName())) {
				return attr;
			}
		}
		return null;
	}

	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		Map<String, String> attrJsonMap = new LinkedHashMap<String, String>();
		for(HAPAttributeInBrick attr : this.m_attributes) {
			attrJsonMap.put(attr.getName(), attr.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildMapJson(attrJsonMap));
		jsonMap.put(BRICKTYPEID, this.m_brickTypeId.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		this.buildJsonMap(jsonMap, typeJsonMap);
		Map<String, String> attrJsonMap = new LinkedHashMap<String, String>();
		for(HAPAttributeInBrick attr : this.m_attributes) {
			attrJsonMap.put(attr.getName(), attr.toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildMapJson(attrJsonMap));
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		for(HAPAttributeInBrick attr : this.m_attributes) {
			dependency.addAll(attr.getResourceDependency(runtimeInfo, resourceManager));
		}
	}
}
