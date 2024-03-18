package com.nosliw.core.application;

import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPWrapperValueInAttributeBrick extends HAPWrapperValueInAttribute implements HAPWithBrick{

	private HAPBrick m_entity;
	
	private HAPIdBrickType m_entityTypeId;
	
	public HAPWrapperValueInAttributeBrick(HAPBrick entity) {
		super(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_BRICK);
		this.m_entity = entity;
		this.m_entityTypeId = this.m_entity.getBrickTypeId();
	}
	
	@Override
	public HAPBrick getBrick() {    return this.m_entity;    }

	@Override
	public HAPIdBrickType getBrickTypeId() {   return this.m_entityTypeId;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(BRICKTYPEID, this.m_entityTypeId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(BRICK, this.getBrick().toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo){
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(BRICK, this.getBrick().toResourceData(runtimeInfo).toString());
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		dependency.addAll(this.getBrick().getResourceDependency(runtimeInfo, resourceManager));
	}
}
