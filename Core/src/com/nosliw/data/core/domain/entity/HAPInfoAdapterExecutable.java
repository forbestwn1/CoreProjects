package com.nosliw.data.core.domain.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

public class HAPInfoAdapterExecutable extends HAPInfoAdapter implements HAPExecutable{

	public HAPInfoAdapterExecutable(String valueType, Object value) {
		super(valueType, value);
	}

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		Object valueObj = this.getValue();
		if(valueObj!=null) {
			if(valueObj instanceof HAPExecutable) jsonMap.put(VALUE, ((HAPExecutable)valueObj).toResourceData(runtimeInfo).toString());
			typeJsonMap.put(VALUE, valueObj.getClass());
		}
		return HAPResourceDataFactory.createJSValueResourceData(HAPUtilityJson.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		if(this.getValue() instanceof HAPExecutable)  out.addAll(((HAPExecutable)this.getValue()).getResourceDependency(runtimeInfo, resourceManager));
		return out;
	}
}
