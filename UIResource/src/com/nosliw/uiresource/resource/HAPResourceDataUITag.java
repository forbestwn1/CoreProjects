package com.nosliw.uiresource.resource;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValue;
import com.nosliw.uiresource.tag.HAPUITagDefinition;

public class HAPResourceDataUITag extends HAPSerializableImp implements HAPResourceDataJSValue{

	private HAPUITagDefinition m_uiTagDefinition;
	
	public HAPResourceDataUITag(HAPUITagDefinition uiUiTagDefinition){
		this.m_uiTagDefinition = uiUiTagDefinition;
	}
	
	public HAPUITagDefinition getUITagDefinition(){ return this.m_uiTagDefinition;  }
	
	@Override
	public String getValue() {
		return this.m_uiTagDefinition.toStringValue(HAPSerializationFormat.JSON_FULL);
	}

}
