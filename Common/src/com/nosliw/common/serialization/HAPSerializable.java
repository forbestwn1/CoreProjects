package com.nosliw.common.serialization;

public interface HAPSerializable {
	
	public String toStringValue(HAPSerializationFormat format);
	
	public void buildObject(Object value, HAPSerializationFormat format);
}
