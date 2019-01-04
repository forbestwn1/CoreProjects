package com.nosliw.data.core.runtime.js;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPResourceDataFactory {

	public static HAPResourceDataJSValue createJSValueResourceData(String value) {
		return new HAPResourceDataJSValueImp(value);
	}

}

class HAPResourceDataJSValueImp extends HAPSerializableImp implements HAPResourceDataJSValue{

	private String m_jsValue;
	
	public HAPResourceDataJSValueImp(String value) {
		this.m_jsValue = value;
	}
	
	@Override
	public String getValue() {  return this.m_jsValue;  }

	@Override
	public String toString() {   return this.getValue();  }
}
