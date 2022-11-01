package com.nosliw.data.core.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

public class HAPAttributeEntityExecutableEntity extends HAPAttributeEntityExecutable<HAPEmbededExecutableWithEntity> implements HAPExecutable{

	public HAPAttributeEntityExecutableEntity(String name, HAPEmbededExecutableWithEntity value) {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_SIMPLE, name, value);
	}

	public HAPAttributeEntityExecutableEntity() {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_SIMPLE);
	}

	@Override
	public boolean getIsComplex() {   return this.getValue().getIsComplex();    }

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUE, this.getValue().toResourceData(runtimeInfo).toString());
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.addAll(this.getValue().getResourceDependency(runtimeInfo, resourceManager));
		return out;
	}

	@Override
	public HAPAttributeEntityExecutableEntity cloneEntityAttribute() {
		HAPAttributeEntityExecutableEntity out = new HAPAttributeEntityExecutableEntity();
		this.cloneToEntityAttribute(out);
		return out;
	}
	
	protected void cloneToEntityAttribute(HAPAttributeEntityExecutableEntity attr) {
		super.cloneToEntityAttribute(attr);
		this.setValue(this.getValue().cloneEmbeded());
	}

}
