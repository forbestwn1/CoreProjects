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
import com.nosliw.data.HAPDataTypeId;
import com.nosliw.data.HAPDataTypePicture;
import com.nosliw.data.HAPOperationOutInfo;
import com.nosliw.data.HAPOperationParmInfo;
import com.nosliw.data.datatype.importer.HAPOperationImp;
import com.nosliw.data.datatype.importer.HAPDataTypeCriteriaImp;
import com.nosliw.data.datatype.importer.HAPDataTypeImp;
import com.nosliw.data.datatype.importer.HAPDataTypeIdImp;
import com.nosliw.data.datatype.importer.HAPDataTypePictureImp;
import com.nosliw.data.datatype.importer.HAPDataTypeVersionImp;
import com.nosliw.data.datatype.importer.js.HAPJSOperationInfo;

public class HAPDBAccess1 extends HAPConfigurableImp{

	private static HAPDBAccess1 m_dbAccess;
	
	// JDBC driver name and database URL
	   static final String m_dbDrive = null;  
	   static final String m_dbUrl = null;

	   //  Database credentials
	   static final String m_dbUser = null;
	   static final String m_dbPw = null;
		
	   Connection m_connection = null;
	   PreparedStatement  m_insertDatatTypeStatement = null;
	   PreparedStatement  m_insertDatatTypePicStatement = null;
	   PreparedStatement  m_insertOperationStatement = null;
	   PreparedStatement  m_insertParmStatement = null;

	   PreparedStatement m_getOperationInfoStatement = null;
	   PreparedStatement m_getOperationInfosByDataTypeInfoStatement = null;
	   PreparedStatement m_getOperationInfosByDataTypeNameStatement = null;
	   PreparedStatement m_getDataTypeByInfoStatement = null;
	   PreparedStatement m_getDataTypeByIdStatement = null;
	   PreparedStatement m_getDataTypesByNameStatement = null;

	   PreparedStatement  m_insertJSOperationStatement = null;
	   
	   private long m_id;
	   
	   public static HAPDBAccess1 getInstance(){
		   if(m_dbAccess==null){
			   m_dbAccess = new HAPDBAccess1();
		   }
		   return m_dbAccess;
	   }
	   
	   private HAPDBAccess1(){
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
			this.m_insertDatatTypeStatement = m_connection.prepareStatement("INSERT INTO DATATYPE (ID, NAME, VERSION, VERSION_MAJOR, VERSION_MINOR, VERSION_REVISION, DESCRIPTION, PARENTINFO, LINKEDVERSION) VALUES (?,?,?,?,?,?,?,?,?)");
			this.m_insertDatatTypePicStatement = m_connection.prepareStatement("INSERT INTO DATATYPE (ID, NAME, VERSION, VERSION_MAJOR, VERSION_MINOR, VERSION_REVISION, DESCRIPTION, PARENTINFO, LINKEDVERSION, NODES) VALUES (?,?,?,?,?,?,?,?,?,?)");
			this.m_insertOperationStatement = m_connection.prepareStatement("INSERT INTO DATAOPERATION (ID, DATATYPENAME, DATATYPEVERSION, NAME, DESCRIPTION) VALUES (?,?,?,?,?)");
			this.m_insertParmStatement = m_connection.prepareStatement("INSERT INTO OPERATIONPARM (ID, OPERATION, TYPE, NAME, DATATYPE, DESCRIPTION) VALUES (?,?,?,?,?,?)");

			this.m_getOperationInfoStatement = m_connection.prepareStatement("SELECT ID, NAME, DESCRIPTION FROM DATAOPERATION WHERE DATATYPENAME=? AND DATATYPEVERSION=? AND NAME=?");
			this.m_getOperationInfosByDataTypeInfoStatement = m_connection.prepareStatement("SELECT ID, DATATYPENAME, DATATYPEVERSION, NAME, DESCRIPTION FROM DATAOPERATION WHERE DATATYPENAME=? AND DATATYPEVERSION=?");
			this.m_getOperationInfosByDataTypeNameStatement = m_connection.prepareStatement("SELECT ID, DATATYPENAME, DATATYPEVERSION, NAME, DESCRIPTION FROM DATAOPERATION WHERE DATATYPENAME=?");
			this.m_getDataTypeByInfoStatement = m_connection.prepareStatement("SELECT ID, NAME, VERSION, DESCRIPTION, PARENTINFO, LINKEDVERSION FROM DATATYPE WHERE NAME=? AND VERSION=?");
			this.m_getDataTypeByIdStatement = m_connection.prepareStatement("SELECT ID, NAME, VERSION, DESCRIPTION, PARENTINFO, LINKEDVERSION FROM DATATYPE WHERE ID=?");
			this.m_getDataTypesByNameStatement = m_connection.prepareStatement("SELECT ID, NAME, VERSION, DESCRIPTION, PARENTINFO, LINKEDVERSION FROM DATATYPE WHERE NAME=?");

			this.m_insertJSOperationStatement = m_connection.prepareStatement("INSERT INTO OPERATIONJS (ID, OPERATION, SCRIPT, RESOURCES) VALUES (?,?,?,?)");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	   
	public void saveOperation(HAPOperationImp operation, HAPDataTypeImp dataType){
		try {
			String operationId = this.getId()+"";
			m_insertOperationStatement.setString(1, operationId);
			m_insertOperationStatement.setString(2, dataType.getId().getName());
			m_insertOperationStatement.setString(3, dataType.getId().getVersion().toStringValue(HAPSerializationFormat.LITERATE));
			m_insertOperationStatement.setString(4, operation.getName());
			m_insertOperationStatement.setString(5, operation.getInfo());
			m_insertOperationStatement.execute();
			
			Map<String, HAPOperationParmInfo> parms = operation.getParmsInfo();
			for(String name : parms.keySet()){
				HAPOperationParmInfo parmInfo = parms.get(name);
				m_insertParmStatement.setString(1, this.getId()+"");
				m_insertParmStatement.setString(2, operationId);
				m_insertParmStatement.setString(3, "parm");
				m_insertParmStatement.setString(4, parmInfo.getName());
				m_insertParmStatement.setString(5, ((HAPDataTypeCriteriaImp)parmInfo.getCriteria()).toStringValue(HAPSerializationFormat.LITERATE));
				m_insertParmStatement.setString(6, parmInfo.getDescription());
				m_insertParmStatement.execute();
			}
			
			HAPOperationOutInfo outputInfo = operation.getOutputInfo();
			if(outputInfo!=null){
				m_insertParmStatement.setString(1, this.getId()+"");
				m_insertParmStatement.setString(2, operationId);
				m_insertParmStatement.setString(3, "output");
				m_insertParmStatement.setString(4, null);
				m_insertParmStatement.setString(5, ((HAPDataTypeIdImp)outputInfo.getDataTypeId()).toStringValue(HAPSerializationFormat.LITERATE));
				m_insertParmStatement.setString(6, outputInfo.getDescription());
				m_insertParmStatement.execute();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private int saveDataTypeToStatement(HAPDataTypeImp dataType, PreparedStatement statement){
		int i = 1;
		
		try {
			HAPDataTypeIdImp dataTypeInfo = (HAPDataTypeIdImp)dataType.getId();
			HAPDataTypeVersionImp dataTypeVersion = (HAPDataTypeVersionImp)dataTypeInfo.getVersion();
			HAPDataTypeIdImp parentDataTypeInfo = (HAPDataTypeIdImp)dataType.getParentInfo();
			HAPDataTypeVersionImp linkedVersion = (HAPDataTypeVersionImp)dataType.getLinkedVersion();

			statement.setString(i++, dataTypeInfo.toStringValue(HAPSerializationFormat.LITERATE));
			statement.setString(i++, dataTypeInfo.getName());
			statement.setString(i++, dataTypeVersion.toStringValue(HAPSerializationFormat.LITERATE));
			statement.setInt(i++, dataTypeVersion.getMajor());
			statement.setInt(i++, dataTypeVersion.getMinor());
			statement.setString(i++, dataTypeVersion.getRevision());
			statement.setString(i++, dataType.getInfo());
			statement.setString(i++, parentDataTypeInfo==null?null:parentDataTypeInfo.toStringValue(HAPSerializationFormat.LITERATE));
			statement.setString(i++, linkedVersion==null?null:linkedVersion.toStringValue(HAPSerializationFormat.LITERATE));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;
	}
	
	public void saveDataType(HAPDataTypeImp dataType){
		this.saveDataTypeToStatement(dataType, m_insertDatatTypeStatement);
		try {
			m_insertDatatTypeStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getDataTypeByInfo(HAPDataTypeId dataTypeInfo, HAPDataTypeImp dataType){
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
				dataType.init(ID, name, version, description, parent, linked);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
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

	public HAPOperationImp getOperationInfo(String dataTypeName, String dataTypeVersion, String operation){
		HAPOperationImp out = null;
		try {
			this.m_getOperationInfoStatement.setString(1, dataTypeName);
			this.m_getOperationInfoStatement.setString(2, dataTypeVersion);
			this.m_getOperationInfoStatement.setString(3, operation);
			ResultSet resultSet = this.m_getOperationInfoStatement.executeQuery();
			if(resultSet.next()){
				String id = resultSet.getString(1);
				String name = resultSet.getString(2);
				String description = resultSet.getString(3);
				out = new HAPOperationImp(id, name, description, dataTypeName, dataTypeVersion);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}

	
	public List<HAPOperationImp> getOperationsInfosByDataTypeInfo(String dataTypeName, String dataTypeVersion){
		List<HAPOperationImp> out = new ArrayList<HAPOperationImp>();
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
				out.add(new HAPOperationImp(id, name, description, dataTypeName, dataTypeVersion));
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
	
	public HAPDataTypePicture getDataTypePicture(String dataTypeId){
		return null;
	}
	
	public void saveDataTypePicture(HAPDataTypePictureImp dataTypePic){
		try {
			int index = this.saveDataTypeToStatement(dataTypePic, m_insertDatatTypePicStatement);
			this.m_insertDatatTypePicStatement.setString(index++, dataTypePic.toStringValue(HAPSerializationFormat.JSON));
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
