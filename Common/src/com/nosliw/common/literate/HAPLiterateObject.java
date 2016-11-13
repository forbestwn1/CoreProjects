package com.nosliw.common.literate;

import java.util.Set;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;

public class HAPLiterateObject implements HAPLiterateDef{

	public String getName() {	return HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT;	}

	@Override
	public Object stringToValue(String strValue, String subType) {
		Object out = null;
		try {
			HAPSerializable serObj = (HAPSerializable)Class.forName(subType).newInstance();
			out = serObj.toStringValue(HAPSerializationFormat.LITERATE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	@Override
	public String valueToString(Object value) {
		HAPSerializable serObj = (HAPSerializable)value;
		return serObj.toStringValue(HAPSerializationFormat.LITERATE);
	}

	@Override
	public Set<Class> getObjectClasses() {
		return null;
	}

	@Override
	public String getSubTypeByObject(Object value) {
		return value.getClass().getName();
	}
}
