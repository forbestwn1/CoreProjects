package com.nosliw.uiresource.resource;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValue;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitResource;

public class HAPResourceDataUIResource extends HAPSerializableImp implements HAPResourceDataJSValue{

	private HAPDefinitionUIUnitResource m_uiResource;
	
	public HAPResourceDataUIResource(HAPDefinitionUIUnitResource uiResource){
		this.m_uiResource = uiResource;
	}
	
	public HAPDefinitionUIUnitResource getUIResource(){ return this.m_uiResource;  }
	
	@Override
	public String getValue() {
		return this.m_uiResource.toStringValue(HAPSerializationFormat.JSON_FULL);
	}

}
