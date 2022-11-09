package com.nosliw.common.serialization;

import com.nosliw.common.interfac.HAPCloneable;

public interface HAPSerializable extends HAPCloneable{
	
	public String toStringValue(HAPSerializationFormat format);
	
	public boolean buildObject(Object value, HAPSerializationFormat format);
}
