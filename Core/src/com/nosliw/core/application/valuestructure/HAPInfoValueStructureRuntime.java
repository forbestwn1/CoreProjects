package com.nosliw.core.application.valuestructure;

import java.util.Map;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.info.HAPInfo;

@HAPEntityWithAttribute
public class HAPInfoValueStructureRuntime extends HAPEntityInfoImp{

	public HAPInfoValueStructureRuntime(String id, HAPInfo info, String name) {
		this.setId(id);
		this.setName(name);
		this.setInfo(info);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
}
