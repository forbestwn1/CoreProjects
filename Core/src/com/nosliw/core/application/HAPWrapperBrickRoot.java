package com.nosliw.core.application;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPWithResourceDependency;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPWrapperBrickRoot extends HAPEntityInfoImp implements HAPWithBrick, HAPWithResourceDependency{

	private HAPBrick m_brick;

	public HAPWrapperBrickRoot(HAPBrick brick) {
		this.m_brick = brick;
	}
	
	@Override
	public HAPBrick getBrick() {   return this.m_brick;     }
	public void setEntity(HAPBrick entity) {     this.m_brick = entity;     }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(BRICK, this.m_brick.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJSJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(BRICK, this.m_brick.toStringValue(HAPSerializationFormat.JAVASCRIPT));
	}

	@Override
	public void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {
		this.m_brick.buildResourceDependency(dependency, runtimeInfo);
	}

}
