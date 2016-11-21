package com.nosliw.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface HAPDataTypeManager {

	//*****************************************  Data Type
	public HAPDataType getDataType(HAPDataTypeInfo dataTypeInfo);
	
	public HAPDataTypeFamily getDataTypeFamily(HAPDataTypeInfo dataTypeInfo);
	
	public List<HAPDataType> queryDataType(HAPQueryInfo queryInfo);
	
	
	//*****************************************  Operation Info
	 //get all available operations info (local, older version, parent)
	public Set<HAPOperationInfo> getOperationInfos(HAPDataTypeInfo dataTypeInfo);
	public HAPOperationInfo getOperationInfoByName(HAPDataTypeInfo dataTypeInfo, String name);
	//get only locally defined operation infos
	public Set<HAPOperationInfo> getLocalOperationInfos(HAPDataTypeInfo dataTypeInfo);
	public HAPOperationInfo getLocalOperationInfoByName(HAPDataTypeInfo dataTypeInfo, String name);
	
	 //get constructor (newData) operations
	public Set<HAPOperationInfo> getNewDataOperations(HAPDataTypeInfo dataTypeInfo);
	
	//get new data operation info by parms type
	public HAPOperationInfo getNewDataOperation(HAPDataTypeInfo dataTypeInfo, Map<String, HAPDataTypeInfo> parmsDataTypeInfos);

	
	
	//*****************************************  
	public Set<String> getLanguages();
	
	public HAPExpression compileExpression(HAPExpressionInfo expressionInfo, String lang);
	
	
}
