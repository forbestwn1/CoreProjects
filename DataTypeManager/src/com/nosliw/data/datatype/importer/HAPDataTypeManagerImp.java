package com.nosliw.data.datatype.importer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeId;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPDataTypeOperation;
import com.nosliw.data.HAPRelationshipPathSegment;
import com.nosliw.data.HAPDataTypePicture;
import com.nosliw.data.HAPDataTypeVersion;
import com.nosliw.data.HAPExpression;
import com.nosliw.data.HAPExpressionInfo;
import com.nosliw.data.HAPOperation;
import com.nosliw.data.datatype.util.HAPDBAccess1;

public class HAPDataTypeManagerImp implements HAPDataTypeManager{

	private HAPDBAccess m_dbAccess;

	public HAPDataTypeManagerImp(){
		this.m_dbAccess = HAPDBAccess.getInstance();
		
		HAPValueInfoManager.getInstance().importFromXML(HAPDataTypeImporterManager.class, new String[]{
				"datatypedefinition.xml",
				"datatypeid.xml",
				"datatypeinfo.xml",
				"operationinfo.xml",
				"datatypeversion.xml",

				"operation.xml",
				"operationvar.xml",
				"datatypeoperation.xml",

				"datatyperelationship.xml"
		});
	}
	
	@Override
	public HAPDataType getDataType(HAPDataTypeId dataTypeInfo) {
		return this.m_dbAccess.getDataType((HAPDataTypeIdImp)dataTypeInfo);
	}

	@Override
	public HAPDataTypeOperation getOperationInfoByName(HAPDataTypeId dataTypeInfo, String name) {
		return this.m_dbAccess.getOperationInfoByName((HAPDataTypeIdImp)dataTypeInfo, name);
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
	
	
	public static void main(String[] args){
		HAPDataTypeManagerImp man = new HAPDataTypeManagerImp();
		HAPDataTypeImp dataType = (HAPDataTypeImp)man.getDataType(new HAPDataTypeIdImp("core.url;1.0.0"));
		System.out.println(dataType.toString());
		
		man.getOperationInfoByName(new HAPDataTypeIdImp("core.url;1.0.0"), "host");
	}
	
}
