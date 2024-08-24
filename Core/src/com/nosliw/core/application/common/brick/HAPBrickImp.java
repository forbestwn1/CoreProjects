package com.nosliw.core.application.common.brick;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public abstract class HAPBrickImp extends HAPSerializableImp implements HAPBrick{

	//all attributes
	private List<HAPAttributeInBrick> m_attributes;
	
	private HAPIdBrickType m_brickTypeId;

	public HAPBrickImp() {
		this.m_attributes = new ArrayList<HAPAttributeInBrick>();
	}
	
	
	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.BRICK;   }

	public void setBrickType(HAPIdBrickType brickTypeId) {   this.m_brickTypeId = brickTypeId;     }
	
	@Override
	public HAPIdBrickType getBrickType() {   return this.m_brickTypeId;     }
	
	@Override
	public List<HAPAttributeInBrick> getAttributes(){     return this.m_attributes;	}
	public HAPAttributeInBrick getAttribute(String attrName) {
		for(HAPAttributeInBrick attr : this.m_attributes) {
			if(attrName.equals(attr.getName())) {
				return attr;
			}
		}
		return null;
	}
	public void setAttribute(HAPAttributeInBrick attribute) {
		for(int i=0; i<this.m_attributes.size(); i++) {
			if(this.m_attributes.get(i).getName().equals(attribute.getName())) {
				this.m_attributes.remove(i);
				break;
			}
		}
		this.m_attributes.add(attribute);
	}
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(BRICKTYPE, this.getBrickType().toStringValue(HAPSerializationFormat.JSON));
		
		List<String> attrJsonList = new ArrayList<String>();
		for(HAPAttributeInBrick attr : this.getAttributes()) {
			attrJsonList.add(attr.toStringValue(HAPSerializationFormat.JAVASCRIPT));
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildArrayJson(attrJsonList.toArray(new String[0])));
		
		jsonMap.put(INTERNALVALUEPORT, this.getInternalValuePorts().toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(EXTERNALVALUEPORT, this.getExternalValuePorts().toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	public void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {
		for(HAPAttributeInBrick attr : this.getAttributes()) {
			attr.buildResourceDependency(dependency, runtimeInfo);
		}
	}

}
