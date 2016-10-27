package com.nosliw.data.datatype.loader;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.clss.HAPClassFilter;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.valueinfo.HAPStringableEntityImporterXML;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.HAPDataOperationInfo;
import com.nosliw.data.HAPDataTypeInfo;
import com.nosliw.data.HAPDataTypeProvider;
import com.nosliw.data.HAPDataTypeVersion;
import com.nosliw.data.imp.HAPDataTypeManagerImp;

public class HAPDataTypeLoaderManager {
	// JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost/nosliw";

   //  Database credentials
   static final String USER = "admin";
   static final String PASS = "admin";
	
   Connection m_connection = null;
   PreparedStatement  m_insertDatatTypeStatement = null;
   
   String m_insertDataTypeSql = "INSERT INTO datatypedef (ID, NAME, VERSION, VERSION_MAJOR, VERSION_MINOR, VERSION_REVISION, DESCRIPTION, PARENTINFO, LINKEDVERSION) VALUES (?,?,?,?,?,?,?,?,?)";
	   
	public HAPDataTypeLoaderManager(){
		registerValueInfos();
		setupDbConnection();
	}
	
	private void setupDbConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		    System.out.println("Connecting to database...");
			m_connection = DriverManager.getConnection(DB_URL,USER,PASS);		
			m_insertDatatTypeStatement = m_connection.prepareStatement(m_insertDataTypeSql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void registerValueInfos(){
		Set<String> valueInfos = new HashSet<String>();
		valueInfos.add("datatypedefinition.xml");
		valueInfos.add("datatypeinfo.xml");
		valueInfos.add("datatypeversion.xml");

		valueInfos.add("dataoperation.xml");
		valueInfos.add("operationoutput.xml");
		valueInfos.add("operationparm.xml");

		HAPValueInfoManager.getInstance().importFromXML(HAPDataTypeLoaderManager.class, valueInfos);
	}
	
	public void loadAll(){
		new HAPClassFilter(){
			@Override
			protected void process(Class cls, Object data) {
				loadDataType(cls);
			}

			@Override
			protected boolean isValid(Class cls) {
				Class[] interfaces = cls.getInterfaces();
				for(Class inf : interfaces){
					if(inf.equals(HAPDataTypeProvider.class)){
						return true;
					}
				}
				return false;
			}
		}.process(null);
	}

	private void loadDataType(Class cls){
		InputStream dataTypeStream = cls.getResourceAsStream("datatype.xml");
		HAPDataTypeImp dataType = (HAPDataTypeImp)HAPStringableEntityImporterXML.readRootEntity(dataTypeStream, "data.datatypedef");
		dataType.resolveByConfigure(null);
		this.saveDataType(dataType);
		
		List<HAPDataOperationInfo> ops = dataType.getDataOperationInfos();
		InputStream opsStream = cls.getResourceAsStream("operations.xml");
		List<HAPStringableValueEntity> ops1 = HAPStringableEntityImporterXML.readMutipleEntitys(opsStream, "data.operation");
		for(HAPStringableValueEntity op : ops1){
			ops.add((HAPDataOperationInfo)op);
		}
		
		for(HAPDataOperationInfo op : ops){
			this.saveOperation((HAPDataOperationInfoImp)op, dataType);
		}
	}

	private void saveOperation(HAPDataOperationInfoImp operation, HAPDataTypeImp dataType){
		
	}
	
	private void saveDataType(HAPDataTypeImp dataType){
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

	private void close(){
		try {
			m_connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		HAPDataTypeLoaderManager man = new HAPDataTypeLoaderManager();
		man.loadAll();
		man.close();
	}
}
