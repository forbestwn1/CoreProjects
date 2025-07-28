package com.nosliw.core.application;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPWrapperValueOfBrick extends HAPWrapperValue implements HAPWithBrick{

	@HAPAttribute
	public static final String BRICK = "brick";
	
	private HAPBrick m_brick;
	
	public HAPWrapperValueOfBrick(HAPBrick brick) {
		super(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_BRICK);
		this.m_brick = brick;
	}
	
	@Override
	public Object getValue() {    return this.getBrick();     }
	
	
	@Override
	public HAPBrick getBrick() {    return this.m_brick;    }

	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJSJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(BRICK, HAPManagerSerialize.getInstance().toStringValue(this.m_brick, HAPSerializationFormat.JAVASCRIPT));
	}

	@Override
	public void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {
		this.m_brick.buildResourceDependency(dependency, runtimeInfo);
	}
}
