package com.nosliw.data.core.domain.container;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

public class HAPContainerEntitySetExecutable extends HAPContainerEntitySet<HAPInfoContainerElementSetExecutable> implements HAPExecutable{

	public HAPContainerEntitySetExecutable() {	}
	
	public HAPContainerEntitySetExecutable(String eleType) {
		super(eleType);
	}
	
	@Override
	public String getContainerType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_EXECUTABLE_SET; }

	@Override
	public HAPContainerEntitySetExecutable cloneContainerEntity() {
		HAPContainerEntitySetExecutable out = new HAPContainerEntitySetExecutable();
		this.cloneToContainer(out);
		return out;
	}

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		
		Map<String, String> byIdJsonMap = new LinkedHashMap<String, String>();
		List<String> elesJsonArray = new ArrayList<String>();
		for(HAPInfoContainerElementSetExecutable ele : this.getAllElementsInfo()) {
			byIdJsonMap.put(ele.getElementId(), ele.toStringValue(HAPSerializationFormat.JSON));
			elesJsonArray.add(ele.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ELEMENTBYID, HAPJsonUtility.buildMapJson(byIdJsonMap));
		jsonMap.put(ELEMENT, HAPJsonUtility.buildArrayJson(elesJsonArray.toArray(new String[0])));
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		for(HAPInfoContainerElementSetExecutable ele : this.getAllElementsInfo()) {
			out.addAll(ele.getResourceDependency(runtimeInfo, resourceManager));
		}
		return out;
	}
}
