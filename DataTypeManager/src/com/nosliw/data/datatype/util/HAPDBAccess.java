package com.nosliw.data.datatype.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.configure.HAPConfigurableImp;
import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.configure.HAPConfigureManager;
import com.nosliw.common.literate.HAPLiterateManager;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.HAPDataTypeInfo;
import com.nosliw.data.HAPDataTypeVersion;
import com.nosliw.data.HAPOperationInfo;
import com.nosliw.data.HAPOperationOutInfo;
import com.nosliw.data.HAPOperationParmInfo;
import com.nosliw.data.datatype.importer.HAPOperationInfoImp;
import com.nosliw.data.datatype.importer.HAPDataTypeCriteriaImp;
import com.nosliw.data.datatype.importer.HAPDataTypeImp;
import com.nosliw.data.datatype.importer.HAPDataTypeInfoImp;
import com.nosliw.data.datatype.importer.HAPDataTypeVersionImp;
import com.nosliw.data.datatype.importer.js.HAPJSOperationInfo;

public class HAPDBAccess extends HAPConfigurableImp{

	private static HAPDBAccess m_dbAccess;
	
	// JDBC driver name and database URL
	   static final String m_dbDrive = null;  
	   static final String m_dbUrl = null;

	   //  Database credentials
	   static final String m_dbUser = null;
	   static final String m_dbPw = null;
		
	   Connection m_connection = null;
	   PreparedStatement  m_insertDatatTypeStatement = null;
	   PreparedStatement  m_insertOperationStatement = null;
	   PreparedStatement  m_insertParmStatement = null;

	   PreparedStatement m_getOperationInfoStatement = null;
	   PreparedStatement m_getOperationInfosByDataTypeInfoStatement = null;
	   PreparedStatement m_getOperationInfosByDataTypeNameStatement = null;
	   PreparedStatement m_getDataTypeByInfoStatement = null;
	   PreparedStatement m_getDataTypesByNameStatement = null;

	   PreparedStatement  m_insertJSOperationStatement = null;
	   
	   private long m_id;
	   
	   public static HAPDBAccess getInstance(){
		   if(m_dbAccess==null){
			   m_dbAccess = new HAPDBAccess();
		   }
		   return m_dbAccess;
	   }
	   
	   private HAPDBAccess(){
		    HAPConfigureImp configure = (HAPConfigureImp)HAPConfigureManager.getInstance().createConfigure().cloneChildConfigure("dataTypeManager.database");
		    this.setConfiguration(configure);
		   
			setupDbConnection();
			this.m_id = System.currentTimeMillis();
	   }
	   
	private long getId(){
		this.m_id++;
		return this.m_id;
	}
	
	public void setupDbConnection(){
		try {
			Class.forName(this.getConfigureValue("jdbc.driver").getStringContent());
		    System.out.println("Connecting to database...");
			m_connection = DriverManager.getConnection(
					this.getConfigureValue("jdbc.url").getStringContent(),
					this.getConfigureValue("username").getStringContent(),
					this.getConfigureValue("password").getStringContent());		
			this.m_insertDatatTypeStatement = m_connection.prepareStatement("INSERT INTO datatypedef (ID, NAME, VERSION, VERSION_MAJOR, VERSION_MINOR, VERSION_REVISION, DESCRIPTION, PARENTINFO, LINKEDVERSION) VALUES (?,?,?,?,?,?,?,?,?)");
			this.m_insertOperationStatement = m_connection.prepareStatement("INSERT INTO DATAOPERATION (ID, DATATYPENAME, DATATYPEVERSION, NAME, DESCRIPTION) VALUES (?,?,?,?,?)");
			this.m_insertParmStatement = m_connection.prepareStatement("INSERT INTO OPERATIONPARM (ID, OPERATION, TYPE, NAME, DATATYPE, DESCRIPTION) VALUES (?,?,?,?,?,?)");

			this.m_getOperationInfoStatement = m_connection.prepareStatement("SELECT ID, NAME, DESCRIPTION FROM DATAOPERATION WHERE DATATYPENAME=? AND DATATYPEVERSION=? AND NAME=?");
			this.m_getOperationInfosByDataTypeInfoStatement = m_connection.prepareStatement("SELECT ID, DATATYPENAME, DATATYPEVERSION, NAME, DESCRIPTION FROM DATAOPERATION WHERE DATATYPENAME=? AND DATATYPEVERSION=?");
			this.m_getOperationInfosByDataTypeNameStatement = m_connection.prepareStatement("SELECT ID, DATATYPENAME, DATATYPEVERSION, NAME, DESCRIPTION FROM DATAOPERATION WHERE DATATYPENAME=?");
			this.m_getDataTypeByInfoStatement = m_connection.prepareStatement("SELECT ID, NAME, VERSION, DESCRIPTION, PARENTINFO, LINKEDVERSION FROM DATATYPEDEF WHERE NAME=? AND VERSION=?");
			this.m_getDataTypesByNameStatement = m_connection.prepareStatement("SELECT ID, NAME, VERSION, DESCRIPTION, PARENTINFO, LINKEDVERSION FROM DATATYPEDEF WHERE NAME=?");

			this.m_insertJSOperationStatement = m_connection.prepareStatement("INSERT INTO OPERATIONJS (ID, OPERATION, SCRIPT, RESOURCES) VALUES (?,?,?,?)");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	   
	public void saveOperation(HAPOperationInfoImp operation, HAPDataTypeImp dataType){
		try {
			String operationId = this.getId()+"";
			m_insertOperationStatement.setString(1, operationId);
			m_insertOperationStatement.setString(2, dataType.getDataTypeInfo().getName());
			m_insertOperationStatement.setString(3, dataType.getDataTypeInfo().getVersion().toStringValue(HAPSerializationFormat.LITERATE));
			m_insertOperationStatement.setString(4, operation.getName());
			m_insertOperationStatement.setString(5, operation.getDescription());
			m_insertOperationStatement.execute();
			
			Map<String, HAPOperationParmInfo> parms = operation.getParmsInfo();
			for(String name : parms.keySet()){
				HAPOperationParmInfo parmInfo = parms.get(name);
				m_insertParmStatement.setString(1, this.getId()+"");
				m_insertParmStatement.setString(2, operationId);
				m_insertParmStatement.setString(3, "parm");
				m_insertParmStatement.setString(4, parmInfo.getName());
				m_insertParmStatement.setString(5, ((HAPDataTypeCriteriaImp)parmInfo.getDataTypeCriteria()).toStringValue(HAPSerializationFormat.LITERATE));
				m_insertParmStatement.setString(6, parmInfo.getDescription());
				m_insertParmStatement.execute();
			}
			
			HAPOperationOutInfo outputInfo = operation.getOutputInfo();
			if(outputInfo!=null){
				m_insertParmStatement.setString(1, this.getId()+"");
				m_insertParmStatement.setString(2, operationId);
				m_insertParmStatement.setString(3, "output");
				m_insertParmStatement.setString(4, null);
				m_insertParmStatement.setString(5, ((HAPDataTypeInfoImp)outputInfo.getDataTypeInfo()).toStringValue(HAPSerializationFormat.LITERATE));
				m_insertParmStatement.setString(6, outputInfo.getDescription());
				m_insertParmStatement.execute();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void saveDataType(HAPDataTypeImp dataType){
		try {
			HAPDataTypeInfoImp dataTypeInfo = (HAPDataTypeInfoImp)dataType.getDataTypeInfo();
			HAPDataTypeVersionImp dataTypeVersion = (HAPDataTypeVersionImp)dataTypeInfo.getVersion();
			HAPDataTypeInfoImp parentDataTypeInfo = (HAPDataTypeInfoImp)dataType.getParentDataTypeInfo();
			HAPDataTypeVersionImp linkedVersion = (HAPDataTypeVersionImp)dataType.getLinkedVersion();
			
			m_insertDatatTypeStatement.setString(1, dataTypeInfo.toStringValue(HAPSerializationFormat.LITERATE));
			m_insertDatatTypeStatement.setString(2, dataTypeInfo.getName());
			m_insertDatatTypeStatement.setString(3, dataTypeVersion.toStringValue(HAPSerializationFormat.LITERATE));
			m_insertDatatTypeStatement.setInt(4, dataTypeVersion.getMajor());
			m_insertDatatTypeStatement.setInt(5, dataTypeVersion.getMinor());
			m_insertDatatTypeStatement.setString(6, dataTypeVersion.getRevision());
			m_insertDatatTypeStatement.setString(7, dataType.getDescription());
			m_insertDatatTypeStatement.setString(8, parentDataTypeInfo==null?null:parentDataTypeInfo.toStringValue(HAPSerializationFormat.LITERATE));
			m_insertDatatTypeStatement.setString(9, linkedVersion==null?null:linkedVersion.toStringValue(HAPSerializationFormat.LITERATE));
			
			m_insertDatatTypeStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public HAPDataTypeImp getDataTypeByInfo(HAPDataTypeInfo dataTypeInfo){
		HAPDataTypeImp out = null;
		try {
			this.m_getDataTypeByInfoStatement.setString(1, dataTypeInfo.getName());
			this.m_getDataTypeByInfoStatement.setString(2, HAPLiterateManager.getInstance().valueToString(dataTypeInfo.getVersion()));
			ResultSet resultSet = this.m_getDataTypeByInfoStatement.executeQuery();
			if(resultSet.next()){
				String ID = resultSet.getString(1);
				String name = resultSet.getString(2);
				String version = resultSet.getString(3);
				String description = resultSet.getString(4);
				String parent = resultSet.getString(5);
				String linked = resultSet.getString(6);
				out = new HAPDataTypeImp(ID, name, version, description, parent, linked);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}

	public List<HAPDataTypeImp> getDataTypesByName(String dataTypeName){
		List<HAPDataTypeImp> out = new ArrayList<HAPDataTypeImp>();
		try {
			this.m_getDataTypesByNameStatement.setString(1, dataTypeName);
			ResultSet resultSet = this.m_getDataTypesByNameStatement.executeQuery();
			while(resultSet.next()){
				String ID = resultSet.getString(1);
				String name = resultSet.getString(2);
				String version = resultSet.getString(3);
				String description = resultSet.getString(4);
				String parent = resultSet.getString(5);
				String linked = resultSet.getString(6);
				out.add(new HAPDataTypeImp(ID, name, version, description, parent, linked));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}

	public HAPOperationInfoImp getOperationInfo(String dataTypeName, String dataTypeVersion, String operation){
		HAPOperationInfoImp out = null;
		try {
			this.m_getOperationInfoStatement.setString(1, dataTypeName);
			this.m_getOperationInfoStatement.setString(2, dataTypeVersion);
			this.m_getOperationInfoStatement.setString(3, operation);
			ResultSet resultSet = this.m_getOperationInfoStatement.executeQuery();
			if(resultSet.next()){
				String id = resultSet.getString(1);
				String name = resultSet.getString(2);
				String description = resultSet.getString(3);
				out = new HAPOperationInfoImp(id, name, description, dataTypeName, dataTypeVersion);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}

	
	public List<HAPOperationInfoImp> getOperationsInfosByDataTypeInfo(HAPDataTypeInfo dataTypeInfo, String operation){
		List<HAPOperationInfoImp> out = new ArrayList<HAPOperationInfoImp>();
		String dataTypeName = dataTypeInfo.getName();
		String dataTypeVersion = null;
		HAPDataTypeVersion version = dataTypeInfo.getVersion();
		if(version!=null)		dataTypeVersion = version.toStringValue(HAPSerializationFormat.LITERATE);
		
		ResultSet resultSet = null;
		try {
			if(HAPBasicUtility.isStringEmpty(dataTypeVersion)){
				this.m_getOperationInfosByDataTypeNameStatement.setString(1, dataTypeName);
				resultSet = this.m_getOperationInfosByDataTypeNameStatement.executeQuery();
			}
			else{
				this.m_getOperationInfosByDataTypeInfoStatement.setString(1, dataTypeName);
				this.m_getOperationInfosByDataTypeInfoStatement.setString(2, dataTypeVersion);
				resultSet = this.m_getOperationInfosByDataTypeInfoStatement.executeQuery();
			}

			while(resultSet.next()){
				String id = resultSet.getString(1);
				dataTypeVersion = resultSet.getString(3);
				String name = resultSet.getString(4);
				String description = resultSet.getString(5);
				out.add(new HAPOperationInfoImp(id, name, description, dataTypeName, dataTypeVersion));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public void saveOperationInfoJS(HAPJSOperationInfo jsOpInfo){
		try {
			this.m_insertJSOperationStatement.setString(1, this.getId()+"");
			this.m_insertJSOperationStatement.setString(2, jsOpInfo.getOperationId());
			this.m_insertJSOperationStatement.setString(3, jsOpInfo.getScript());
			this.m_insertJSOperationStatement.setString(4, HAPLiterateManager.getInstance().valueToString(jsOpInfo.getResources()));
			
			this.m_insertJSOperationStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void close(){
		try {
			m_connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
