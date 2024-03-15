package com.nosliw.data.core.entity;

import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPInfoAttributeValueEntity extends HAPInfoAttributeValue implements HAPWithEntity{

	private HAPEntityExecutable m_entity;
	
	private HAPIdEntityType m_entityTypeId;
	
	public HAPInfoAttributeValueEntity(HAPEntityExecutable entity) {
		super(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_ENTITY);
		this.m_entity = entity;
		this.m_entityTypeId = this.m_entity.getEntityTypeId();
	}
	
	@Override
	public HAPEntityExecutable getEntity() {    return this.m_entity;    }

	@Override
	public HAPIdEntityType getEntityTypeId() {   return this.m_entityTypeId;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITYTYPEID, this.m_entityTypeId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ENTITY, this.getEntity().toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo){
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITY, this.getEntity().toResourceData(runtimeInfo).toString());
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		dependency.addAll(this.getEntity().getResourceDependency(runtimeInfo, resourceManager));
	}
}
