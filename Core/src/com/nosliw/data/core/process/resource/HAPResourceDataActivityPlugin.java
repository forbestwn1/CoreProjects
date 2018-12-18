package com.nosliw.data.core.process.resource;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValue;

public class HAPResourceDataActivityPlugin extends HAPSerializableImp implements HAPResourceDataJSValue{

	private String m_script;
	
	public HAPResourceDataActivityPlugin(String script){
		this.m_script = script;
	}
	
	@Override
	public String getValue() {
		return this.m_script;
	}

}
