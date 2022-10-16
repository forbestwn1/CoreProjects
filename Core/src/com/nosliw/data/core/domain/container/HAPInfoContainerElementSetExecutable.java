package com.nosliw.data.core.domain.container;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPEmbededWithExecutable;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

public class HAPInfoContainerElementSetExecutable extends HAPInfoContainerElementSet<HAPEmbededWithExecutable> implements HAPExecutable{

	private String m_elementId;

	public HAPInfoContainerElementSetExecutable(HAPEmbededWithExecutable embededEntity, String elementId) {
		super(embededEntity);
		this.m_elementId = elementId;
	}

	public HAPInfoContainerElementSetExecutable() {	}

	@Override
	public String getInfoType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_EXECUTABLE_SET;    }

	@Override
	public String getElementId() {  return this.m_elementId;  }

	@Override
	public HAPInfoContainerElementSetExecutable cloneContainerElementInfo() {
		HAPInfoContainerElementSetExecutable out = new HAPInfoContainerElementSetExecutable();
		this.cloneToInfoContainerElement(out);
		return out;
	}
	
	protected void cloneToInfoContainerElement(HAPInfoContainerElementSetExecutable containerEleInfo) {
		super.cloneToInfoContainerElement(containerEleInfo);
		containerEleInfo.m_elementId = this.m_elementId;
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
