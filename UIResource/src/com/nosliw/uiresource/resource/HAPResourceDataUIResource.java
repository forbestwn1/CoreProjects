package com.nosliw.uiresource.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValueImp;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;

public class HAPResourceDataUIResource extends HAPResourceDataJSValueImp{

	private HAPExecutableUIUnitPage m_uiResource;
	
	public HAPResourceDataUIResource(HAPExecutableUIUnitPage uiResource){
		this.m_uiResource = uiResource;
	}
	
	public HAPExecutableUIUnitPage getUIResource(){ return this.m_uiResource;  }
	
	@Override
	public String getValue() {
		return this.m_uiResource.toStringValue(HAPSerializationFormat.JSON_FULL);
	}

}
