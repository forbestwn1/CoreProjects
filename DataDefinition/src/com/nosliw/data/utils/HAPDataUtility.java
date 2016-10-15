package com.nosliw.data.utils;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.datatype.HAPDataTypeInfo;
import com.nosliw.data.datatype.HAPDataTypeInfoWithVersion;

public class HAPDataUtility {

	public static HAPDataTypeInfo getDataTypeInfo(HAPData data){return data.getDataType().getDataTypeInfo().toDataTypeInfo();}
	public static HAPDataTypeInfoWithVersion getDataTypeInfoWithVersion(HAPData data){return data.getDataType().getDataTypeInfo();}
	
	public static boolean isBooleanType(HAPData data){
		HAPDataTypeInfo dataTypeInfo = getDataTypeInfo(data);
		return HAPConstant.DATATYPE_CATEGARY_SIMPLE.equals(dataTypeInfo.getCategary()) &&
				HAPConstant.DATATYPE_TYPE_BOOLEAN.equals(dataTypeInfo.getType());
	}

	public static boolean isIntegerType(HAPData data){
		HAPDataTypeInfo dataTypeInfo = getDataTypeInfo(data);
		return HAPConstant.DATATYPE_CATEGARY_SIMPLE.equals(dataTypeInfo.getCategary()) &&
				HAPConstant.DATATYPE_TYPE_INTEGER.equals(dataTypeInfo.getType());
	}

	public static boolean isStringType(HAPData data){
		HAPDataTypeInfo dataTypeInfo = getDataTypeInfo(data);
		return HAPConstant.DATATYPE_CATEGARY_SIMPLE.equals(dataTypeInfo.getCategary()) &&
				HAPConstant.DATATYPE_TYPE_STRING.equals(dataTypeInfo.getType());
	}
	
	public static boolean isFloatType(HAPData data){
		HAPDataTypeInfo dataTypeInfo = getDataTypeInfo(data);
		return HAPConstant.DATATYPE_CATEGARY_SIMPLE.equals(dataTypeInfo.getCategary()) &&
				HAPConstant.DATATYPE_TYPE_FLOAT.equals(dataTypeInfo.getType());
	}
	
	/*
	public static String buildConstantOperationDataJson(HAPData data){
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();

		jsonMap.put("type", String.valueOf(data.getOperandType()));
		jsonMap.put("data", data.toStringValue(HAPSerializationFormat.JSON_DATATYPE));
		
		return HAPJsonUtility.getMapJson(jsonMap);
	}
*/
}
