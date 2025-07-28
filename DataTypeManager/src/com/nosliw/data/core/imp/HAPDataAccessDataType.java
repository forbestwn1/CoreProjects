package com.nosliw.data.core.imp;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.nosliw.common.literate.HAPLiterateManager;
import com.nosliw.common.strvalue.valueinfo.HAPDBTableInfo;
import com.nosliw.common.strvalue.valueinfo.HAPDBUtility;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.data.HAPDataTypeId;
import com.nosliw.core.data.HAPOperationParmInfo;
import com.nosliw.core.data.HAPRelationship;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.imp.io.HAPDBSource;
import com.nosliw.data.core.imp.io.HAPDataAccess;

public class HAPDataAccessDataType extends HAPDataAccess{

	public HAPDataAccessDataType(HAPValueInfoManager valueInfoMan, HAPDBSource dbSource) {
		super(valueInfoMan, dbSource);
	}

	
	public void saveDataTypeOperation(List<HAPDataTypeOperationImp> dataTypeOperations){
		for(HAPDataTypeOperationImp dataTypeOperation : dataTypeOperations){
			this.saveEntity(dataTypeOperation, this.getConnection());
		}
	}
	
	public void saveOperation(HAPOperationImp operation, HAPDataTypeImp dataType){
		operation.updateAtomicChildObjectValue(HAPOperationImp.DATATYPNAME, dataType.getName());
		this.saveEntity(operation, this.getConnection());
		
		List<HAPOperationParmInfo> parms = operation.getParmsInfo();
		for(HAPOperationParmInfo p : parms){
			HAPOperationVarInfoImp parm = (HAPOperationVarInfoImp)p;
			parm.setType(HAPConstantShared.DATAOPERATION_VAR_TYPE_IN);
			parm.updateAtomicChildStrValue(HAPOperationVarInfoImp.OPERATIONID, operation.getId());
			parm.updateAtomicChildObjectValue(HAPOperationVarInfoImp.DATATYPEID, dataType.getName());
			this.saveEntity(parm, this.getConnection());
		}
		
		HAPOperationVarInfoImp output = (HAPOperationVarInfoImp)operation.getOutputInfo();
		if(output!=null){
			output.setType(HAPConstantShared.DATAOPERATION_VAR_TYPE_OUT);
			output.updateAtomicChildStrValue(HAPOperationVarInfoImp.OPERATIONID, operation.getId());
			output.updateAtomicChildObjectValue(HAPOperationVarInfoImp.DATATYPEID, dataType.getName());
			this.saveEntity(output, this.getConnection());
		}
	}

	public void saveDataTypePicture(HAPDataTypePictureImp pic){
		Set<? extends HAPRelationship> relationships = pic.getRelationships();
		HAPDataTypeImp sourceDataTypeImp = (HAPDataTypeImp)pic.getSourceDataType();
		
		for(HAPRelationship relationship : relationships){
			this.saveEntity((HAPRelationshipImp)relationship, this.getConnection());
		}
	}
	
	public void saveDataType(HAPDataTypeImp dataType) {
		this.saveToDB(dataType, this.getConnection());
	}
	
	public HAPDataTypeOperationImp getDataTypeOperation(HAPDataTypeId dataTypeId, String operationName){
		HAPDataTypeOperationImp out = (HAPDataTypeOperationImp)this.queryEntityFromDB(HAPDataTypeOperationImp._VALUEINFO_NAME, "source=? AND name=?", new Object[]{dataTypeId.getFullName(), operationName}, this.getConnection());
		if(out!=null) {
			List<HAPOperationVarInfoImp> parms = (List<HAPOperationVarInfoImp>)this.queryEntitysFromDB(HAPOperationVarInfoImp._VALUEINFO_NAME, "operationId=?", new Object[]{out.getOperationId()}, this.getConnection());
			for(HAPOperationVarInfoImp parm : parms){
				if(HAPConstantShared.DATAOPERATION_VAR_TYPE_IN.equals(parm.getType())){
					out.addParmsInfo(parm);
				}
				else if(HAPConstantShared.DATAOPERATION_VAR_TYPE_OUT.equals(parm.getType())){
					out.setOutputInfo(parm);
				}
			}
		}
		else {
			System.err.println("Operation does not exist : "  + dataTypeId.toString() + "  " + operationName);
		}
		
		return out;
	}
	
	public List<HAPDataTypeOperationImp> getDataTypeOperations(HAPDataTypeId dataTypeId){
		return (List<HAPDataTypeOperationImp>)this.queryEntitysFromDB(HAPDataTypeOperationImp._VALUEINFO_NAME, "source=?", new Object[]{dataTypeId.getName()}, this.getConnection());
	}
	
	public List<HAPDataTypeOperationImp> getNormalDataTypeOperations(HAPDataTypeId dataTypeId){
		return (List<HAPDataTypeOperationImp>)this.queryEntitysFromDB(HAPDataTypeOperationImp._VALUEINFO_NAME, "type='"+HAPConstantShared.DATAOPERATION_TYPE_NORMAL+"' AND source=?", new Object[]{dataTypeId.getName()}, this.getConnection());
	}
	
	public HAPDataTypeFamilyImp getDataTypeFamily(HAPDataTypeId dataTypeId){
		HAPDataTypeFamilyImp out = null;
		List<HAPRelationshipImp> relationships = this.getRelationships(dataTypeId, null);
		if(relationships.size()>0){
			HAPDataTypeImp targetDataType = this.getDataType(dataTypeId);
			out = new HAPDataTypeFamilyImp(targetDataType);
			for(HAPRelationshipImp relationship : relationships){
				out.addRelationship(relationship);
			}
		}
		return out;
	}

	public HAPRelationshipImp getRelationship(HAPDataTypeId sourceDataTypeId, HAPDataTypeId targetDataTypeId){
		return (HAPRelationshipImp)this.queryEntityFromDB(HAPRelationshipImp._VALUEINFO_NAME, "sourceDataType_fullName=? AND targetDataType_fullName=?", new Object[]{sourceDataTypeId.getFullName(), targetDataTypeId.getFullName()}, this.getConnection());
	}
	
	public List<HAPRelationshipImp> getRelationships(HAPDataTypeId sourceDataTypeId, String targetType){
		if(HAPUtilityBasic.isStringEmpty(targetType)){
			return (List<HAPRelationshipImp>)this.queryEntitysFromDB(HAPRelationshipImp._VALUEINFO_NAME, "targetDataType_fullName=?", new Object[]{sourceDataTypeId.getFullName()}, this.getConnection());
		}
		else{
			return (List<HAPRelationshipImp>)this.queryEntitysFromDB(HAPRelationshipImp._VALUEINFO_NAME, "targetDataType_fullName=? AND targetType=?", new Object[]{sourceDataTypeId.getFullName(), targetType}, this.getConnection());
		}
	}
	
	public HAPDataTypePictureImp getDataTypePicture(HAPDataTypeId dataTypeId){
		HAPDataTypePictureImp out = null;
		
		try {
			String valuInfoName = HAPRelationshipImp._VALUEINFO_NAME;
			HAPDBTableInfo dbTableInfo = HAPValueInfoManager.getInstance().getDBTableInfo(valuInfoName);
			String sql = HAPDBUtility.buildEntityQuerySql(dbTableInfo.getTableName(), "sourceDataType_fullName=?");

			PreparedStatement statement = this.getConnection().prepareStatement(sql);
			statement.setString(1, dataTypeId.getFullName());

			List<Object> results = HAPDBUtility.queryFromDB(valuInfoName, statement);
			if(results.size()>0){
				HAPDataTypeImp sourceDataType = this.getDataType(dataTypeId);
				out = new HAPDataTypePictureImp(sourceDataType);
				for(Object result : results){
					out.addRelationship((HAPRelationshipImp)result);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}

	public List<HAPOperationImp> getOperationInfosByDataType(HAPDataTypeId dataTypeName){
		return (List<HAPOperationImp>)this.queryEntitysFromDB(HAPOperationImp._VALUEINFO_NAME, "dataTypeName=?", new Object[]{dataTypeName.getFullName()}, this.getConnection());
	}

	public HAPOperationImp getOperationInfoByName(HAPDataTypeId dataTypeName, String name) {
		HAPOperationImp operationInfo = this.getOperationBasicInfoByName(dataTypeName, name);

		List<HAPOperationVarInfoImp> parms = (List<HAPOperationVarInfoImp>)this.queryEntitysFromDB(HAPOperationVarInfoImp._VALUEINFO_NAME, "operationId=?", new Object[]{operationInfo.getId()}, this.getConnection());
		for(HAPOperationVarInfoImp parm : parms){
			if(HAPConstantShared.DATAOPERATION_VAR_TYPE_IN.equals(parm.getType())){
				operationInfo.addParmsInfo(parm);
			}
			else if(HAPConstantShared.DATAOPERATION_VAR_TYPE_OUT.equals(parm.getType())){
				operationInfo.setOutputInfo(parm);
			}
		}
		return operationInfo;
	}
	
	public HAPOperationImp getOperationBasicInfoByName(HAPDataTypeId dataTypeName, String name) {
		return (HAPOperationImp)this.queryEntityFromDB(HAPOperationImp._VALUEINFO_NAME, "name=? AND dataTypeName=?", new Object[]{name, dataTypeName.getFullName()}, this.getConnection());
	}

	public HAPOperationImp getOperationBasicInfoById(String id) {
		return (HAPOperationImp)this.queryEntityFromDB(HAPOperationImp._VALUEINFO_NAME, "id=?", new Object[]{id}, this.getConnection());
	}
	
	public List<HAPDataTypeImp> getAllDataTypes(){
		return this.queryEntitysFromDB(HAPDataTypeImpLoad._VALUEINFO_NAME, "", null, this.getConnection());
	}
	
	public HAPDataTypeImp getDataType(HAPDataTypeId dataTypeInfo) {
		return (HAPDataTypeImp)this.queryEntityFromDB(HAPDataTypeImpLoad._VALUEINFO_NAME, "name=? AND versionFullName=?",
				new Object[]{dataTypeInfo.getName(), HAPLiterateManager.getInstance().valueToString(dataTypeInfo.getVersionFullName())}, this.getConnection());
	}
	
}
