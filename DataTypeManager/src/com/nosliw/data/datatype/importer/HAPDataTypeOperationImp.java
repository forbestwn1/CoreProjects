package com.nosliw.data.datatype.importer;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.HAPDataTypeOperation;
import com.nosliw.data.HAPRelationship;
import com.nosliw.data.HAPOperation;

public class HAPDataTypeOperationImp extends HAPOperationImp implements HAPDataTypeOperation{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String OPERATIONID = "operationId";

	@HAPAttribute
	public static String TARGET = "target";

	@HAPAttribute
	public static String SOURCE = "source";
	
	@HAPAttribute
	public static String PATH = "path";
	
	public HAPDataTypeOperationImp(HAPOperationImp op){
		
	}
	
	@Override
	public HAPOperation getOperationInfo() {  return this;  }

	@Override
	public HAPRelationship getTargetDataType() {
		return null;
	}

}
