package com.nosliw.common.displayresource;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPDisplayResourceLeaf extends HAPDisplayResource{

	@HAPAttribute
	public static String VALUE = "value";

	private String m_value;
	
	public HAPDisplayResourceLeaf(String value) {
		this();
		this.m_value = value;
	}

	public HAPDisplayResourceLeaf() {}
	
	public String getValue() {   return this.m_value;   }

	@Override
	public HAPDisplayResource cloneDisplayResource() {
		HAPDisplayResourceLeaf out = new HAPDisplayResourceLeaf();
		out.m_value = this.m_value;
		return out;
	}

	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format){
		if(value instanceof String)   this.m_value = (String)value;
		return true;
	}
	
	@Override
	public String toStringValue(HAPSerializationFormat format) {
		return this.m_value;
	}
}
