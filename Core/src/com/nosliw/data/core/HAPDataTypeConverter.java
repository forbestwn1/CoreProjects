package com.nosliw.data.core;

/**
 * Converter id
 * Converter do the job of converting one data type to another
 * @author ewaniwa
 *
 */
public class HAPDataTypeConverter extends HAPOperationId{

	public HAPDataTypeConverter(String literate){
		super(literate);
	}
	
	public HAPDataTypeConverter(String name, String version, String operation){
		super(name, version, operation);
	}
		
	public HAPDataTypeConverter(String name, HAPDataTypeVersion version, String operation){
		super(name, version, operation);
	}

	public HAPDataTypeConverter(String fullName, String operation){
		super(fullName, operation);
	}
	
	public HAPDataTypeConverter(HAPDataTypeId dataTypeId, String operation){
		super(dataTypeId, operation);
	}
	
}
