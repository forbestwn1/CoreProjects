package com.nosliw.common.literate;

import java.lang.reflect.Type;
import java.util.List;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;

public class HAPLiterateObject implements HAPLiterateDef{

	public String getName() {	return HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT;	}

	@Override
	public Object stringToValue(String strValue, String subType) {
		HAPSerializable out = null;
		try {
			 out = (HAPSerializable)Class.forName(subType).newInstance();
		} catch (Exception e) {
//			e.printStackTrace();
		}
		try {
			if(out!=null)	 out.buildObject(strValue, HAPSerializationFormat.LITERATE);
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
	public List<Class> getObjectClasses() {
		return null;
	}

	@Override
	public String getSubTypeByType(Type value) {
		return ((Class)value).getName();
	}
}
