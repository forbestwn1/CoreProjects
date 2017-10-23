package com.nosliw.uiresource.resource;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValue;
import com.nosliw.uiresource.definition.HAPUIDefinitionUnitResource;

public class HAPResourceDataUIResource extends HAPSerializableImp implements HAPResourceDataJSValue{

	private HAPUIDefinitionUnitResource m_uiResource;
	
	public HAPResourceDataUIResource(HAPUIDefinitionUnitResource uiResource){
		this.m_uiResource = uiResource;
	}
	
	public HAPUIDefinitionUnitResource getUIResource(){ return this.m_uiResource;  }
	
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
	}
	
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildFullJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	public String getValue() {
		return this.m_uiResource.toStringValue(HAPSerializationFormat.JSON);
	}

}
