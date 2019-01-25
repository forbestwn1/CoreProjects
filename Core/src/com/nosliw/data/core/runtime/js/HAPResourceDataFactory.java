package com.nosliw.data.core.runtime.js;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPResourceDataFactory {

	public static HAPResourceDataJSValue createJSValueResourceData(String value) {
		return new HAPResourceDataJSValueInternal(value);
	}

}

class HAPResourceDataJSValueInternal extends HAPResourceDataJSValueImp{

	private String m_jsValue;
	
	public HAPResourceDataJSValueInternal(String value) {
		this.m_jsValue = value;
	}
	
	@Override
	public String getValue() {  return this.m_jsValue;  }

}
