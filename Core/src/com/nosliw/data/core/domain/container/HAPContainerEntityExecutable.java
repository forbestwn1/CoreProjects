package com.nosliw.data.core.domain.container;

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

public abstract class HAPContainerEntityExecutable<T extends HAPElementContainerExecutable> extends HAPContainerEntity<T> implements HAPExecutable{

	public HAPContainerEntityExecutable() {	}

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		
		List<String> elesJsonArray = new ArrayList<String>();
		for(T ele : this.getAllElements()) {
			String resourceStr = ((HAPExecutable)ele).toResourceData(runtimeInfo).toString();
			elesJsonArray.add(resourceStr);
		}
		jsonMap.put(ELEMENT, HAPUtilityJson.buildArrayJson(elesJsonArray.toArray(new String[0])));
		return HAPResourceDataFactory.createJSValueResourceData(HAPUtilityJson.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		for(T ele : this.getAllElements()) {
			out.addAll(((HAPExecutable)ele).getResourceDependency(runtimeInfo, resourceManager));
		}
		return out;
	}
}
