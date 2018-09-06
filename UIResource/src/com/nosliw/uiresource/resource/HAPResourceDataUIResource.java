package com.nosliw.uiresource.resource;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValue;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitResource;

public class HAPResourceDataUIResource extends HAPSerializableImp implements HAPResourceDataJSValue{

	private HAPExecutableUIUnitResource m_uiResource;
	
	public HAPResourceDataUIResource(HAPExecutableUIUnitResource uiResource){
		this.m_uiResource = uiResource;
	}
	
	public HAPExecutableUIUnitResource getUIResource(){ return this.m_uiResource;  }
	
	@Override
	public String getValue() {
		return this.m_uiResource.toStringValue(HAPSerializationFormat.JSON_FULL);
	}

}
