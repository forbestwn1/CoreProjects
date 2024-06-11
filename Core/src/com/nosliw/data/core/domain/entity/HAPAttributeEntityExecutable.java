package com.nosliw.data.core.domain.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.HAPInfoBrickType;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

public class HAPAttributeEntityExecutable extends HAPAttributeEntity<HAPEmbededExecutable> implements HAPExecutable{

	private HAPExecutableEntity m_parentEntity;
	

	public HAPAttributeEntityExecutable(String name, HAPEmbededExecutable value, HAPInfoBrickType valueTypeInfo) {
		super(name, value, valueTypeInfo);
	}

	public HAPAttributeEntityExecutable() {}

	public boolean isAttributeAutoProcess() {	return this.isAttributeAutoProcess(false);	}

	public HAPExecutableEntity getEmbededValueEntity() {   return this.getValue().getEntityValue();    }
	
	public HAPExecutableEntity getParentEntity() {    return this.m_parentEntity;   }
	public void setParentEntity(String attrName, HAPExecutableEntity parent) {     
		this.m_parentEntity = parent;

		HAPEmbededExecutable executableEmbeded = this.getValue();
		Object value = executableEmbeded.getValue();
		if(value instanceof HAPExecutableEntity) {
			((HAPExecutableEntity)value).setParent(attrName, m_parentEntity);
		}
	}
	
	protected void cloneToEntityAttribute(HAPAttributeEntityExecutable attr) {
		super.cloneToEntityAttribute(attr);
		this.setValue((HAPEmbededExecutable)this.getValue().cloneEmbeded());
	}

	@Override
	public HAPAttributeEntity cloneEntityAttribute() {
		HAPAttributeEntityExecutable out = new HAPAttributeEntityExecutable();
		this.cloneToEntityAttribute(out);
		return out;
	}
	
	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		if(this.getValue() instanceof HAPExecutable) jsonMap.put(VALUE, ((HAPExecutable)this.getValue()).toResourceData(runtimeInfo).toString());
		return HAPResourceDataFactory.createJSValueResourceData(HAPUtilityJson.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		if(this.getValue() instanceof HAPExecutable)    out.addAll(((HAPExecutable)this.getValue()).getResourceDependency(runtimeInfo, resourceManager));
		return out;
	}
}
