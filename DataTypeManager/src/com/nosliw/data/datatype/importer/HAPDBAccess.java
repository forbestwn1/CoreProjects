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
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeId;
import com.nosliw.data.HAPDataTypeOperation;
import com.nosliw.data.HAPOperationParmInfo;
import com.nosliw.data.HAPRelationship;

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

	public HAPDataTypePictureImp getDataTypePicture(HAPDataTypeIdImp dataTypeId){
		HAPDataTypePictureImp out = null;
		
		try {
			String valuInfoName = "data.relationship";
			HAPDBTableInfo dbTableInfo = HAPValueInfoManager.getInstance().getDBTableInfo(valuInfoName);
			String sql = HAPSqlUtility.buildEntityQuerySql(dbTableInfo.getTableName(), "source=?");

			PreparedStatement statement = m_connection.prepareStatement(sql);
			statement.setString(1, dataTypeId.getName());

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
	
	
	public void saveOperation(HAPOperationImp operation, HAPDataTypeImp dataType){
		operation.updateAtomicChildStrValue(HAPOperationImp.ID, this.getId()+"");
		operation.updateAtomicChildObjectValue(HAPOperationImp.DATATYPID, dataType.getName());
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
	
	public HAPDataTypeOperation getOperationInfoByName(HAPDataTypeIdImp dataTypeInfo, String name) {
		HAPDataTypeOperationImp out = null;
		
		try {
			String valuInfoName = "data.operation";
			HAPDBTableInfo dbTableInfo = HAPValueInfoManager.getInstance().getDBTableInfo(valuInfoName);
			String sql = HAPSqlUtility.buildEntityQuerySql(dbTableInfo.getTableName(), "name=? AND dataTypeName=? AND versionFullName=?");

			PreparedStatement statement = m_connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setString(2, dataTypeInfo.getName());
			statement.setString(3, dataTypeInfo.getVersionFullName());

			List<Object> results = HAPSqlUtility.queryFromDB(valuInfoName, statement);
			HAPOperationImp operationInfo = null;
			if(results.size()>0)  operationInfo = (HAPOperationImp)results.get(0);
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public List<HAPDataTypeImp> getAllDataTypes(){
		List out = new ArrayList<HAPDataTypeImp>();
		
		try {
			String valuInfoName = "data.datatypedef";
			HAPDBTableInfo dbTableInfo = HAPValueInfoManager.getInstance().getDBTableInfo(valuInfoName);
			String sql = HAPSqlUtility.buildEntityQuerySql(dbTableInfo.getTableName(), "");

			PreparedStatement statement = m_connection.prepareStatement(sql);
			out = HAPSqlUtility.queryFromDB(valuInfoName, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public void saveDataTypePicture(HAPDataTypePictureImp pic){
		Set<? extends HAPRelationship> relationships = pic.getRelationships();
		HAPDataTypeImp sourceDataTypeImp = (HAPDataTypeImp)pic.getSourceDataType();
		
		for(HAPRelationship relationship : relationships){
			((HAPRelationshipImp)relationship).setId(this.getId()+"");
			HAPSqlUtility.saveToDB((HAPStringableValueEntity)relationship, m_connection);
		}
	}
	
	
	public HAPDataTypeImp getDataType(HAPDataTypeIdImp dataTypeInfo) {
		HAPDataTypeImp out = null;
		
		try {
			String valuInfoName = "data.datatypedef";
			HAPDBTableInfo dbTableInfo = HAPValueInfoManager.getInstance().getDBTableInfo(valuInfoName);
			String sql = HAPSqlUtility.buildEntityQuerySql(dbTableInfo.getTableName(), "name=? AND versionFullName=?");

			PreparedStatement statement = m_connection.prepareStatement(sql);
			
			statement.setString(1, dataTypeInfo.getName());
			statement.setString(2, HAPLiterateManager.getInstance().valueToString(dataTypeInfo.getVersionFullName()));

			List<Object> results = HAPSqlUtility.queryFromDB(valuInfoName, statement);
			if(results.size()>0)  out = (HAPDataTypeImp)results.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
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
}
