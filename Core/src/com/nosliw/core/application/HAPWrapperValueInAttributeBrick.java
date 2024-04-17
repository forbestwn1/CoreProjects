package com.nosliw.core.application;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPWrapperValueInAttributeBrick extends HAPWrapperValueInAttribute implements HAPWithBrick{

	@HAPAttribute
	public static final String BRICK = "brick";
	
	private HAPBrick m_brick;
	
	public HAPWrapperValueInAttributeBrick(HAPBrick brick) {
		super(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_BRICK);
		this.m_brick = brick;
	}
	
	@Override
	public Object getValue() {    return this.getBrick();     }
	
	
	@Override
	public HAPBrick getBrick() {    return this.m_brick;    }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(BRICK, HAPSerializeManager.getInstance().toStringValue(this.m_brick, HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo){
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(BRICK, this.m_brick.toResourceData(runtimeInfo).toString());
	}
}
