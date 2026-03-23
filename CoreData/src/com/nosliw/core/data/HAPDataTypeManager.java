package com.nosliw.core.data;

import java.util.List;
import java.util.Map;

public interface HAPDataTypeManager {

	//*****************************************  Data Type
	public HAPDataType getDataType(HAPDataTypeId dataTypeInfo);
//	public HAPDataTypePicture buildDataTypePicture(HAPDataTypeInfo dataTypeInfo);
	
	//*****************************************  Operation Info
	 //get all available operations info (local, older version, parent)
	public List<? extends HAPDataTypeOperation> getOperationInfos(HAPDataTypeId dataTypeInfo);
	public HAPDataTypeOperation getOperationInfoByName(HAPDataTypeId dataTypeInfo, String name);
	

	//*****************************************   Expression
//	public HAPExpression compileExpression(HAPExpressionInfo expressionInfo);
	

	
	
	
	
	 //get constructor (newData) operations
//	public List<? extends HAPOperationInfo> getNewDataOperations(HAPDataTypeInfo dataTypeInfo);
	
	//get new data operation info by parms type
//	public HAPOperationInfo getNewDataOperation(HAPDataTypeInfo dataTypeInfo, Map<String, HAPDataTypeInfo> parmsDataTypeInfos);

	
	//*****************************************  
//	public List<String> getLanguages();
	
	
}
