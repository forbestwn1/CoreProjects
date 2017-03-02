package com.nosliw.common.serialization;

public interface HAPSerializable {
	
	public String toStringValue(HAPSerializationFormat format);
	
	public boolean buildObject(Object value, HAPSerializationFormat format);
}
