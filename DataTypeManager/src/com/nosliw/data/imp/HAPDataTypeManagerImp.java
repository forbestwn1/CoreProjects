package com.nosliw.data.imp;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.HAPOperationInfo;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeFamily;
import com.nosliw.data.HAPDataTypeInfo;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPExpression;
import com.nosliw.data.HAPExpressionInfo;
import com.nosliw.data.HAPQueryInfo;

public class HAPDataTypeManagerImp implements HAPDataTypeManager{

	@Override
	public HAPDataType getDataType(HAPDataTypeInfo dataTypeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPDataTypeFamily getDataTypeFamily(HAPDataTypeInfo dataTypeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HAPDataType> queryDataType(HAPQueryInfo queryInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HAPOperationInfo> getOperationInfos(HAPDataTypeInfo dataTypeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPOperationInfo getOperationInfoByName(HAPDataTypeInfo dataTypeInfo, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HAPOperationInfo> getLocalOperationInfos(HAPDataTypeInfo dataTypeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPOperationInfo getLocalOperationInfoByName(HAPDataTypeInfo dataTypeInfo, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HAPOperationInfo> getNewDataOperations(HAPDataTypeInfo dataTypeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPOperationInfo getNewDataOperation(HAPDataTypeInfo dataTypeInfo,
			Map<String, HAPDataTypeInfo> parmsDataTypeInfos) {
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
