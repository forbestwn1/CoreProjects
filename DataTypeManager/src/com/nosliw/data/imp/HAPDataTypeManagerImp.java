package com.nosliw.data.imp;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.HAPOperation;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeFamily;
import com.nosliw.data.HAPDataTypeId;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPExpression;
import com.nosliw.data.HAPExpressionInfo;
import com.nosliw.data.HAPQueryInfo;

public class HAPDataTypeManagerImp implements HAPDataTypeManager{

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
	public Set<HAPOperation> getOperationInfos(HAPDataTypeId dataTypeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPOperation getOperationInfoByName(HAPDataTypeId dataTypeInfo, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HAPOperation> getLocalOperationInfos(HAPDataTypeId dataTypeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPOperation getLocalOperationInfoByName(HAPDataTypeId dataTypeInfo, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HAPOperation> getNewDataOperations(HAPDataTypeId dataTypeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPOperation getNewDataOperation(HAPDataTypeId dataTypeInfo,
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
