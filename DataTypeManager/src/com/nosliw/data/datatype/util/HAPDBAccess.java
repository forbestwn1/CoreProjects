package com.nosliw.data.datatype.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import com.nosliw.common.configure.HAPConfigurableImp;
import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.configure.HAPConfigureManager;
import com.nosliw.data.HAPDataOperationOutInfo;
import com.nosliw.data.HAPDataOperationParmInfo;
import com.nosliw.data.datatype.importer.HAPDataOperationInfoImp;
import com.nosliw.data.datatype.importer.HAPDataTypeImp;
import com.nosliw.data.datatype.importer.HAPDataTypeInfoImp;
import com.nosliw.data.datatype.importer.HAPDataTypeVersionImp;

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
	   
	   String m_insertDataTypeSql = "INSERT INTO datatypedef (ID, NAME, VERSION, VERSION_MAJOR, VERSION_MINOR, VERSION_REVISION, DESCRIPTION, PARENTINFO, LINKEDVERSION) VALUES (?,?,?,?,?,?,?,?,?)";
	   String m_insertOperationSql = "INSERT INTO DATAOPERATION (ID, DATATYPE, NAME, DESCRIPTION) VALUES (?,?,?,?)";
	   String m_insertParmSql = "INSERT INTO OPERATIONPARM (ID, OPERATION, TYPE, NAME, DATATYPE, DESCRIPTION) VALUES (?,?,?,?,?,?)";

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
			m_insertDatatTypeStatement = m_connection.prepareStatement(m_insertDataTypeSql);
			m_insertOperationStatement = m_connection.prepareStatement(m_insertOperationSql);
			m_insertParmStatement = m_connection.prepareStatement(m_insertParmSql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	   
	public void saveOperation(HAPDataOperationInfoImp operation, HAPDataTypeImp dataType){
		try {
			String operationId = this.getId()+"";
			m_insertOperationStatement.setString(1, operationId);
			m_insertOperationStatement.setString(2, ((HAPDataTypeInfoImp)dataType.getDataTypeInfo()).getStringValue());
			m_insertOperationStatement.setString(3, operation.getName());
			m_insertOperationStatement.setString(4, operation.getDescription());
			m_insertOperationStatement.execute();
			
			Map<String, HAPDataOperationParmInfo> parms = operation.getParmsInfo();
			for(String name : parms.keySet()){
				HAPDataOperationParmInfo parmInfo = parms.get(name);
				m_insertParmStatement.setString(1, this.getId()+"");
				m_insertParmStatement.setString(2, operationId);
				m_insertParmStatement.setString(3, "parm");
				m_insertParmStatement.setString(4, parmInfo.getName());
				m_insertParmStatement.setString(5, ((HAPDataTypeInfoImp)parmInfo.getDataTypeInfo()).getStringValue());
				m_insertParmStatement.setString(6, parmInfo.getDescription());
				m_insertParmStatement.execute();
			}
			
			HAPDataOperationOutInfo outputInfo = operation.getOutputInfo();
			if(outputInfo!=null){
				m_insertParmStatement.setString(1, this.getId()+"");
				m_insertParmStatement.setString(2, operationId);
				m_insertParmStatement.setString(3, "output");
				m_insertParmStatement.setString(4, null);
				m_insertParmStatement.setString(5, ((HAPDataTypeInfoImp)outputInfo.getDataTypeInfo()).getStringValue());
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
			
			m_insertDatatTypeStatement.setString(1, dataTypeInfo.getStringValue());
			m_insertDatatTypeStatement.setString(2, dataTypeInfo.getName());
			m_insertDatatTypeStatement.setString(3, dataTypeVersion.getStringValue());
			m_insertDatatTypeStatement.setInt(4, dataTypeVersion.getMajor());
			m_insertDatatTypeStatement.setInt(5, dataTypeVersion.getMinor());
			m_insertDatatTypeStatement.setString(6, dataTypeVersion.getRevision());
			m_insertDatatTypeStatement.setString(7, dataType.getDescription());
			m_insertDatatTypeStatement.setString(8, parentDataTypeInfo==null?null:parentDataTypeInfo.getStringValue());
			m_insertDatatTypeStatement.setString(9, linkedVersion==null?null:linkedVersion.getStringValue());
			
			m_insertDatatTypeStatement.execute();
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
