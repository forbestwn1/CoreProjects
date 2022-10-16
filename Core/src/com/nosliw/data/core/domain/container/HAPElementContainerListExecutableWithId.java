package com.nosliw.data.core.domain.container;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPEmbededWithIdExecutable;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

public class HAPElementContainerListExecutableWithId extends HAPElementContainerList<HAPEmbededWithIdExecutable> implements HAPExecutable{

	public HAPElementContainerListExecutableWithId(HAPEmbededWithIdExecutable embededEntity, String elementId) {
		super(embededEntity, elementId);
	}

	public HAPElementContainerListExecutableWithId() {}

	@Override
	public String getInfoType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_EXECUTABLE_LIST;    }

	@Override
	public HAPElementContainerListExecutableWithId cloneContainerElementInfo() {
		HAPElementContainerListExecutableWithId out = new HAPElementContainerListExecutableWithId();
		this.cloneToInfoContainerElement(out);
		return out;
	}
	
	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITY, this.getEmbededElementEntity().toResourceData(runtimeInfo).toString());
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.addAll(this.getEmbededElementEntity().getResourceDependency(runtimeInfo, resourceManager));
		return out;
	}
}
