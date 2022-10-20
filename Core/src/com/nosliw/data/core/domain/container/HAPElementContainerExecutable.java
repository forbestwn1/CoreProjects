package com.nosliw.data.core.domain.container;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.data.core.domain.HAPEmbededExecutable;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

public class HAPElementContainerExecutable<T extends HAPEmbededExecutable> extends HAPElementContainer<T> implements HAPExecutable{

	public HAPElementContainerExecutable(T embededEntity, String elementId) {
		super(embededEntity, elementId);
	}

	public HAPElementContainerExecutable() {	}

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITY, (this.getEmbededElementEntity()).toResourceData(runtimeInfo).toString());
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.addAll((this.getEmbededElementEntity()).getResourceDependency(runtimeInfo, resourceManager));
		return out;
	}

	@Override
	public HAPElementContainerExecutable cloneContainerElement() {
		HAPElementContainerExecutable out = new HAPElementContainerExecutable();
		this.cloneToContainerElement(out);
		return out;
	}
}
