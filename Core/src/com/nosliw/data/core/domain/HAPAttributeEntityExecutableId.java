package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

public class HAPAttributeEntityExecutableId extends HAPAttributeEntityExecutable<HAPEmbededExecutableWithId> implements HAPExecutable{

	public HAPAttributeEntityExecutableId(String name, HAPEmbededExecutableWithId value) {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_SIMPLE, name, value);
	}

	public HAPAttributeEntityExecutableId() {
		super(HAPConstantShared.ENTITYATTRIBUTE_TYPE_SIMPLE);
	}

	@Override
	public boolean getIsComplex() {   return this.getValue().getIsComplex();    }

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}
	
	@Override
	public HAPAttributeEntityExecutableId cloneEntityAttribute() {
		HAPAttributeEntityExecutableId out = new HAPAttributeEntityExecutableId();
		this.cloneToEntityAttribute(out);
		return out;
	}
	
	protected void cloneToEntityAttribute(HAPAttributeEntityExecutableId attr) {
		super.cloneToEntityAttribute(attr);
		this.setValue(this.getValue().cloneEmbeded());
	}
	
}
