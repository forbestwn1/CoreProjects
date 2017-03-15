package com.nosliw.data.datatype.importer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.configure.HAPConfigurableImp;
import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.configure.HAPConfigureManager;
import com.nosliw.common.literate.HAPLiterateManager;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.valueinfo.HAPDBTableInfo;
import com.nosliw.common.strvalue.valueinfo.HAPSqlUtility;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.core.HAPDataType;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataTypeOperation;
import com.nosliw.data.core.HAPOperation;
import com.nosliw.data.core.HAPOperationParmInfo;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.datatype.importer.js.HAPJSOperation;

public class HAPDBAccess extends HAPConfigurableImp {

	private static HAPDBAccess m_dbAccess;

	// JDBC driver name and database URL
	static final String m_dbDrive = null;
	static final String m_dbUrl = null;

	// Database credentials
	static final String m_dbUser = null;
	static final String m_dbPw = null;

	Connection m_connection = null;
	private long m_id;

	public static HAPDBAccess getInstance() {
		if (m_dbAccess == null) {
			m_dbAccess = new HAPDBAccess();
		}
		return m_dbAccess;
	}

	private HAPDBAccess() {
		HAPConfigureImp configure = (HAPConfigureImp) HAPConfigureManager.getInstance().createConfigure()
				.cloneChildConfigure("dataTypeManager.database");
		this.setConfiguration(configure);

		setupDbConnection();
		this.m_id = System.currentTimeMillis();
	}

	public List<HAPDataTypeOperationImp> getDataTypeOperations(HAPDataTypeId dataTypeId){
		return (List<HAPDataTypeOperationImp>)this.queryEntitysFromDB("data.datatypeoperation", "sourceDataTypeName=?", new Object[]{dataTypeId.getName()});
	}
	
	public List<HAPDataTypeOperationImp> getNormalDataTypeOperations(HAPDataTypeId dataTypeId){
		return (List<HAPDataTypeOperationImp>)this.queryEntitysFromDB("data.datatypeoperation", "type=null && sourceDataTypeName=?", new Object[]{dataTypeId.getName()});
	}
	
	public HAPDataTypeFamilyImp getDataTypeFamily(HAPDataTypeIdImp dataTypeId){
		HAPDataTypeFamilyImp out = null;
		
		try {
			String valuInfoName = "data.relationship";
			HAPDBTableInfo dbTableInfo = HAPValueInfoManager.getInstance().getDBTableInfo(valuInfoName);
			String sql = HAPSqlUtility.buildEntityQuerySql(dbTableInfo.getTableName(), "target_fullName=?");

			PreparedStatement statement = m_connection.prepareStatement(sql);
			statement.setString(1, dataTypeId.getFullName());

			List<Object> results = HAPSqlUtility.queryFromDB(valuInfoName, statement);
			if(results.size()>0){
				HAPDataTypeImp targetDataType = this.getDataType(dataTypeId);
				out = new HAPDataTypeFamilyImp(targetDataType);
				for(Object result : results){
					out.addRelationship((HAPRelationshipImp)result);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}

	public HAPDataTypePictureImp getDataTypePicture(HAPDataTypeIdImp dataTypeId){
		HAPDataTypePictureImp out = null;
		
		try {
			String valuInfoName = "data.relationship";
			HAPDBTableInfo dbTableInfo = HAPValueInfoManager.getInstance().getDBTableInfo(valuInfoName);
			String sql = HAPSqlUtility.buildEntityQuerySql(dbTableInfo.getTableName(), "source_fullName=?");

			PreparedStatement statement = m_connection.prepareStatement(sql);
			statement.setString(1, dataTypeId.getFullName());

			List<Object> results = HAPSqlUtility.queryFromDB(valuInfoName, statement);
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
	
	public void saveOperationJS(HAPJSOperation operationJs){
		operationJs.setId(this.getId()+"");
		HAPSqlUtility.saveToDB(operationJs, this.m_connection);
	}
	
	public HAPJSOperation getJSOperation(HAPDataTypeIdImp dataTypeId, String operation){
		return (HAPJSOperation)this.queryEntityFromDB(
				HAPJSOperation.DATADEFINITION_NAME, 
				HAPJSOperation.DATATYPENAME+"=? AND +"+HAPJSOperation.OPERATIONNAME+"=?",
				new Object[]{dataTypeId.getFullName(), operation});
		
	}
	
	public void saveDataTypeOperation(List<HAPDataTypeOperationImp> dataTypeOperations){
		for(HAPDataTypeOperationImp dataTypeOperation : dataTypeOperations){
			dataTypeOperation.setId(this.getId()+"");
			HAPSqlUtility.saveToDB(dataTypeOperation, this.m_connection);
		}
	}
	
	public void saveOperation(HAPOperationImp operation, HAPDataTypeImp dataType){
		operation.updateAtomicChildStrValue(HAPOperationImp.ID, this.getId()+"");
		operation.updateAtomicChildObjectValue(HAPOperationImp.DATATYPNAME, dataType.getName());
		HAPSqlUtility.saveToDB(operation, m_connection);
		
		Map<String, HAPOperationParmInfo> parms = operation.getParmsInfo();
		for(String name : parms.keySet()){
			HAPOperationVarInfoImp parm = (HAPOperationVarInfoImp)parms.get(name);
			parm.updateAtomicChildStrValue(HAPOperationVarInfoImp.ID, this.getId()+"");
			parm.updateAtomicChildStrValue(HAPOperationVarInfoImp.OPERATIONID, operation.getId());
			parm.updateAtomicChildObjectValue(HAPOperationVarInfoImp.DATATYPEID, dataType.getName());
			HAPSqlUtility.saveToDB(parm, this.m_connection);
		}		
	}

	public List<HAPOperationImp> getOperationInfosByDataType(HAPDataTypeIdImp dataTypeName){
		return (List<HAPOperationImp>)this.queryEntitysFromDB("data.operation", "dataTypeName=?", new Object[]{dataTypeName.getFullName()});
	}
	
	public HAPDataTypeOperationImp getOperationInfoByName(HAPDataTypeIdImp dataTypeName, String name) {
		return (HAPDataTypeOperationImp)this.queryEntityFromDB("data.operation", "name=? AND dataTypeName=?", new Object[]{name, dataTypeName.getFullName()});
	}
	
	public List<HAPDataTypeImp> getAllDataTypes(){
		return this.queryEntitysFromDB("data.datatypedef", "", null);
	}
	
	public void saveDataTypePicture(HAPDataTypePictureImp pic){
		Set<? extends HAPRelationship> relationships = pic.getRelationships();
		HAPDataTypeImp sourceDataTypeImp = (HAPDataTypeImp)pic.getSourceDataType();
		
		for(HAPRelationship relationship : relationships){
			((HAPRelationshipImp)relationship).setId(this.getId()+"");
			HAPSqlUtility.saveToDB((HAPStringableValueEntity)relationship, m_connection);
		}
	}
	
	
	public HAPDataTypeImp getDataType(HAPDataTypeId dataTypeInfo) {
		return (HAPDataTypeImp)this.queryEntityFromDB("data.datatypedef", "name=? AND versionFullName=?",
				new Object[]{dataTypeInfo.getName(), HAPLiterateManager.getInstance().valueToString(((HAPDataTypeIdImp)dataTypeInfo).getVersionFullName())});
	}
	
	
	
	public void saveDataType(HAPDataTypeImp dataType) {
		HAPSqlUtility.saveToDB(dataType, m_connection);
	}

	public void createDBTable(String dataTypeName) {
		HAPDBTableInfo tableInfo = HAPValueInfoManager.getInstance().getDBTableInfo(dataTypeName);
		HAPSqlUtility.createDBTable(tableInfo, m_connection);
	}

	public void setupDbConnection() {
		try {
			Class.forName(this.getConfigureValue("jdbc.driver").getStringContent());
			System.out.println("Connecting to database...");
			m_connection = DriverManager.getConnection(this.getConfigureValue("jdbc.url").getStringContent(),
					this.getConfigureValue("username").getStringContent(),
					this.getConfigureValue("password").getStringContent());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private long getId(){
		this.m_id++;
		return this.m_id;
	}
	
	private List queryEntitysFromDB(String valueInfoName, String query, Object[] parms){
		List<Object> out = new ArrayList<Object>();
		
		try {
			HAPDBTableInfo dbTableInfo = HAPValueInfoManager.getInstance().getDBTableInfo(valueInfoName);
			String sql = HAPSqlUtility.buildEntityQuerySql(dbTableInfo.getTableName(), query);

			PreparedStatement statement = m_connection.prepareStatement(sql);
			if(parms!=null){
				for(int i=0; i<parms.length; i++){
					statement.setObject(i+1, parms[i]);
				}
			}

			out = HAPSqlUtility.queryFromDB(valueInfoName, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}
	
	private Object queryEntityFromDB(String valueInfoName, String query, Object[] parms){
		Object out = null;
		List entitys = this.queryEntitysFromDB(valueInfoName, query, parms);
		if(entitys.size()>0)  out = entitys.get(0);
		return out;
	}
}
