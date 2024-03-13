package com.nosliw.data.core.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
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
	
	public HAPAttributeExecutable getAttribute(String attrName) {
		for(HAPAttributeExecutable attr : this.m_attributes) {
			if(attrName.equals(attr.getName())) {
				return attr;
			}
		}
		return null;
	}

	
	public HAPAttributeExecutable getDescendantAttribute(HAPPath path) {
		HAPAttributeExecutable out = null;
		for(int i=0; i<path.getLength(); i++) {
			String attribute = path.getPathSegments()[i];
			if(i==0) {
				out = this.getAttribute(attribute);
			} else {
				HAPInfoAttributeValue attrValueInfo = out.getValueInfo();
				String attrValueType = attrValueInfo.getValueType();
				if(attrValueInfo instanceof HAPWithEntity) {
					out = ((HAPWithEntity)attrValueInfo).getEntity().getAttribute(attribute);
				}
				else{
					throw new RuntimeException();
				}
			}
		}
		return out;
	}

	public HAPEntityExecutable getDescendantEntity(HAPPath path) {
		HAPEntityExecutable out = null;
		if(path==null||path.isEmpty()) {
			out = this;
		} else {
			HAPInfoAttributeValue attrValueInfo = this.getDescendantAttribute(path).getValueInfo();
			String attrValueType = attrValueInfo.getValueType();
			if(attrValueInfo instanceof HAPWithEntity) {
				out = ((HAPWithEntity)attrValueInfo).getEntity();
			}
			else {
				throw new RuntimeException();
			}
		}
		return out;
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
	
}
