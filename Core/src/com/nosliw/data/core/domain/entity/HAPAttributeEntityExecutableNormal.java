package com.nosliw.data.core.domain.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

public abstract class HAPAttributeEntityExecutableNormal<T extends HAPEmbededExecutable> extends HAPAttributeEntityExecutable<T> implements HAPExecutable{

	public HAPAttributeEntityExecutableNormal(String name, T value) {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_NORMAL, name, value);
	}

	public HAPAttributeEntityExecutableNormal() {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_NORMAL);
	}

	@Override
	public boolean getIsComplex() {   return this.getValue().getIsComplex();    }

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUE, this.getValue().toResourceData(runtimeInfo).toString());
		return HAPResourceDataFactory.createJSValueResourceData(HAPUtilityJson.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.addAll(this.getValue().getResourceDependency(runtimeInfo, resourceManager));
		return out;
	}

	protected void cloneToEntityAttribute(HAPAttributeEntityExecutableNormal attr) {
		super.cloneToEntityAttribute(attr);
		this.setValue((T)this.getValue().cloneEmbeded());
	}
}
