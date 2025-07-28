package com.nosliw.common.serialization;

public class HAPUtilityJavaScript {

	public static String buildConstantValue(Object obj) {
		if(obj instanceof String) {
			return "'"+obj+"'";
		} else if(obj instanceof Integer) {
			return ""+obj;
		} else if(obj instanceof Boolean) {
			return ""+obj;
		} else if(obj instanceof Float) {
			return ""+obj;
		} else if(obj instanceof Double) {
			return ""+obj;
		} else {
			return HAPManagerSerialize.getInstance().toStringValue(obj, HAPSerializationFormat.JSON);
		}
	}
}
