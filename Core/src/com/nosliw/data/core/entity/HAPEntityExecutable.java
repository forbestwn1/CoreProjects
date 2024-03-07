package com.nosliw.data.core.entity;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPExecutableImp;

public class HAPEntityExecutable extends HAPExecutableImp{

	//all attributes
	private List<HAPAttributeExecutable> m_attributes;
	
	private HAPIdEntityType m_entityTypeId;
	
	
	public HAPEntityExecutable() {
		this.m_attributes = new ArrayList<HAPAttributeExecutable>();
	}
	
	public List<HAPAttributeExecutable> getAttributes(){     return this.m_attributes;      }
	
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
				if(attrValueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_ENTITY)) {
					out = ((HAPInfoAttributeValueEntity)attrValueInfo).getEntity().getAttribute(attribute);
				}
				else if(attrValueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID)) {
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
			if(attrValueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_ENTITY)) {
				out = ((HAPInfoAttributeValueEntity)attrValueInfo).getEntity();
			}
			else if(attrValueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID)) {
				throw new RuntimeException();
			}
		}
		return out;
	}
}
