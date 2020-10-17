package com.nosliw.data.core.imp;

import java.util.List;

import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.strvalue.valueinfo.HAPDBAccess;
import com.nosliw.data.core.data.HAPDataType;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.data.HAPDataTypeManager;
import com.nosliw.data.core.data.HAPDataTypeOperation;

public class HAPDataTypeManagerImp implements HAPDataTypeManager{

	private HAPDataAccessDataType m_dataAccess;

	public HAPDataTypeManagerImp(HAPDataAccessDataType dataAccess){
		init();
		this.m_dataAccess = dataAccess;
	}
	
	public HAPDBAccess getDBAccess(){		return this.m_dataAccess;	}
	
	private void init(){
		
		HAPSerializeManager.getInstance().registerClassName(HAPDataTypeId.class.getName(), HAPDataTypeId.class.getName());
	}
	
	@Override
	public HAPDataType getDataType(HAPDataTypeId dataTypeInfo) {
		return this.m_dataAccess.getDataType(dataTypeInfo);
	}

	@Override
	public HAPDataTypeOperation getOperationInfoByName(HAPDataTypeId dataTypeInfo, String name) {
//		return this.m_dbAccess.getOperationInfoByName((HAPDataTypeIdImp)dataTypeInfo, name);
		return null;
	}
	
	
	@Override
	public List<? extends HAPDataTypeOperation> getOperationInfos(HAPDataTypeId dataTypeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

/*	
	@Override
	public HAPDataType getDataType(HAPDataTypeInfo dataTypeInfo) {
		return this.m_dbAccess.getDataTypeByInfo(dataTypeInfo);
	}

	public List<? extends HAPOperationInfo> getLocalOperationInfos(HAPDataTypeInfo dataTypeInfo) {
		String dataTypeVersion = null;
		HAPDataTypeVersion version = dataTypeInfo.getVersion();
		dataTypeVersion = version.toStringValue(HAPSerializationFormat.LITERATE);
		return this.m_dbAccess.getOperationsInfosByDataTypeInfo(dataTypeInfo.getName(), dataTypeVersion);
	}

	public HAPOperationInfo getLocalOperationInfoByName(HAPDataTypeInfo dataTypeInfo, String name) {
		return this.m_dbAccess.getOperationInfo(dataTypeInfo.getName(), dataTypeInfo.getVersion().toStringValue(HAPSerializationFormat.LITERATE), name);
	}

	@Override
	public List<? extends HAPOperationInfo> getOperationInfos(HAPDataTypeInfo dataTypeInfo) {
		
		
		
		String dataTypeVersion = null;
		HAPDataTypeVersion version = dataTypeInfo.getVersion();
		if(version!=null){
			dataTypeVersion = version.toStringValue(HAPSerializationFormat.LITERATE);
		}
		return this.m_dbAccess.getOperationsInfosByDataTypeInfo(dataTypeInfo.getName(), dataTypeVersion);
	}

	@Override
	public HAPOperationInfo getOperationInfoByName(HAPDataTypeInfo dataTypeInfo, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends HAPOperationInfo> getNewDataOperations(HAPDataTypeInfo dataTypeInfo) {
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
	public HAPExpression compileExpression(HAPExpressionInfo expressionInfo, String lang) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HAPDataType> queryDataType(HAPQueryInfo queryInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getLanguages() {
		// TODO Auto-generated method stub
		return null;
	}

	private HAPDataTypeImpOperations buildDataTypeOperations(HAPDataTypeInfoImp dataTypeInfo){
		HAPDataTypeImpOperations out = new HAPDataTypeImpOperations();
		
		m_dbAccess.getDataTypeByInfo(dataTypeInfo, out);
		
		//from connected operations
		this.buildDataTypeOperationsFromConnectedDataType(out, HAPConstant.DATATYPE_PATHSEGMENT_PARENT);
		this.buildDataTypeOperationsFromConnectedDataType(out, HAPConstant.DATATYPE_PATHSEGMENT_LINKED);
		
		//from own operation
		List<HAPOperationInfoImp> ops = this.m_dbAccess.getOperationsInfosByDataTypeInfo(dataTypeInfo.getName(), dataTypeInfo.getVersion().toStringValue(HAPSerializationFormat.LITERATE));
		for(HAPOperationInfoImp op : ops){
			out.addOperation(new HAPDataTypeOperationImp(op));
		}
		return out;
	}
	
	private void buildDataTypeOperationsFromConnectedDataType(HAPDataTypeImpOperations out, int connectType){
		HAPDataTypeInfoImp connectDataTypeInfo = (HAPDataTypeInfoImp)out.getConntectedDataTypeInfo(connectType);
		HAPDataTypeImpOperations connectDataTypeOps = this.getDataTypeOperations(connectDataTypeInfo);
		if(connectDataTypeOps==null){
			connectDataTypeOps = this.buildDataTypeOperations(connectDataTypeInfo);
		}
		
		for(HAPDataTypeOperationImp dataTypeOp : connectDataTypeOps.getOperations()){
			dataTypeOp.getTargetDataType().appendPathSegment(HAPDataTypePathSegment.buildPathSegment(connectType));
			out.addOperation(dataTypeOp);
		}
		
	}
	
	private HAPDataTypeImpOperations getDataTypeOperations(HAPDataTypeInfoImp dataTypeInfo){
		
	}
	
*/	
	
	
//	public static void main(String[] args){
//		HAPDataTypeManagerImp man = new HAPDataTypeManagerImp();
//		HAPDataTypeImp dataType = (HAPDataTypeImp)man.getDataType(new HAPDataTypeId("core.url;1.0.0"));
//		System.out.println(dataType.toString());
//		
//		man.getOperationInfoByName(new HAPDataTypeId("core.url;1.0.0"), "host");
//	}
	
}
