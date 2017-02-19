package com.nosliw.data.imp;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.HAPOperationInfo;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeFamily;
import com.nosliw.data.HAPDataTypeId;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPExpression;
import com.nosliw.data.HAPExpressionInfo;
import com.nosliw.data.HAPQueryInfo;

public class HAPDataTypeManagerImp implements HAPDataTypeManager{

	public HAPDataTypeManagerImp(){
	}
	
	public void registerDataType(HAPDataType dataType){
		System.out.println("*****************************************************");
		System.out.println(dataType.toString());
	}
	
	public void registerDataTypeOperations(HAPDataTypeId dataTypeImp){
		
	}
	
	
	@Override
	public HAPDataType getDataType(HAPDataTypeId dataTypeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPDataTypeFamily getDataTypeFamily(HAPDataTypeId dataTypeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HAPDataType> queryDataType(HAPQueryInfo queryInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HAPOperationInfo> getOperationInfos(HAPDataTypeId dataTypeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPOperationInfo getOperationInfoByName(HAPDataTypeId dataTypeInfo, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HAPOperationInfo> getLocalOperationInfos(HAPDataTypeId dataTypeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPOperationInfo getLocalOperationInfoByName(HAPDataTypeId dataTypeInfo, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HAPOperationInfo> getNewDataOperations(HAPDataTypeId dataTypeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPOperationInfo getNewDataOperation(HAPDataTypeId dataTypeInfo,
			Map<String, HAPDataTypeId> parmsDataTypeInfos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getLanguages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPExpression compileExpression(HAPExpressionInfo expressionInfo, String lang) {
		// TODO Auto-generated method stub
		return null;
	}

}
