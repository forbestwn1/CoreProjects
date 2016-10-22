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
	public Set<HAPDataOperationInfo> getOperationInfos(HAPDataTypeInfo dataTypeInfo);
	public HAPDataOperationInfo getOperationInfoByName(HAPDataTypeInfo dataTypeInfo, String name);
	//get only locally defined operation infos
	public Set<HAPDataOperationInfo> getLocalOperationInfos(HAPDataTypeInfo dataTypeInfo);
	public HAPDataOperationInfo getLocalOperationInfoByName(HAPDataTypeInfo dataTypeInfo, String name);
	
	 //get constructor (newData) operations
	public Set<HAPDataOperationInfo> getNewDataOperations(HAPDataTypeInfo dataTypeInfo);
	
	//get new data operation info by parms type
	public HAPDataOperationInfo getNewDataOperation(HAPDataTypeInfo dataTypeInfo, Map<String, HAPDataTypeInfo> parmsDataTypeInfos);

	
	
	//*****************************************  
	public Set<String> getLanguages();
	
	public HAPExpression compileExpression(HAPExpressionInfo expressionInfo, String lang);
	
	
}
