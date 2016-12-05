package com.nosliw.data.datatype.importer;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;

public class HAPDataTypeImpOperations extends HAPDataTypeImp{

	@HAPAttribute
	public static String OPERATIONS = "operations";
	
	public HAPDataTypeImpOperations(){}
	
	public HAPDataTypeImpOperations(String Id, String name, String version, String description, String parent, String linked) {
		super(Id, name, version, description, parent, linked);
	}

	public List<HAPDataTypeOperationImp> getOperations(){
		return null;
	}
	
	public void addOperation(HAPDataTypeOperationImp dataTypeOperation){
		
	}
}
