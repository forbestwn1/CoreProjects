package com.nosliw.data.core.data;

import com.nosliw.common.utils.HAPConstantShared;

/**
 * Converter id
 * Converter do the job of converting one data type to another
 * @author ewaniwa
 *
 */
public class HAPDataTypeConverter extends HAPOperationId{

	public HAPDataTypeConverter(String fullName){
		super(fullName, HAPConstantShared.DATAOPERATION_TYPE_CONVERT);
	}
	
	public HAPDataTypeConverter(String name, String version){
		super(name, version, HAPConstantShared.DATAOPERATION_TYPE_CONVERT);
	}
		
	public HAPDataTypeConverter(String name, HAPDataTypeVersion version){
		super(name, version, HAPConstantShared.DATAOPERATION_TYPE_CONVERT);
	}

	public HAPDataTypeConverter(HAPDataTypeId dataTypeId){
		super(dataTypeId, HAPConstantShared.DATAOPERATION_TYPE_CONVERT);
	}
	
}
