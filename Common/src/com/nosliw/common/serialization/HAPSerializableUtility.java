package com.nosliw.common.serialization;

public class HAPSerializableUtility {

	public static String toLiterateString(Object obj){
		if(obj==null)  return null;
		if(obj instanceof HAPSerializable){
			return ((HAPSerializable)obj).toStringValue(HAPSerializationFormat.LITERATE);
		}
		else{
			return obj.toString();
		}
	}
	
}
