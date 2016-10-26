package com.nosliw.data.datatype.loader;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.clss.HAPClassFilter;
import com.nosliw.common.strvalue.valueinfo.HAPStringableEntityImporterXML;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
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
	
	   Connection conn = null;
	   PreparedStatement  stmt = null;
	   
	   String m_addDataTypeSql = "INSERT INTO datatypedef (ID, NAME, VERSION, VERSION_MAJOR, VERSION_MINOR, VERSION_REVISION, DESCRIPTION, PARENTINFO, LINKEDVERSION) VALUES (?,?,?,?,?,?,?,?,?)";
	   
	public HAPDataTypeLoaderManager(){
		
	      try {
	  		//STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
		      //STEP 3: Open a connection
		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);		
		      stmt = conn.prepareStatement(m_addDataTypeSql);
		      
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadAll(){
		Set<String> valueInfos = new HashSet<String>();
		valueInfos.add("datatypedefinition.xml");
		valueInfos.add("datatypeinfo.xml");
		valueInfos.add("datatypeversion.xml");
		HAPValueInfoManager.getInstance().importFromXML(HAPDataTypeLoaderManager.class, valueInfos);
		
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
		InputStream stream = cls.getResourceAsStream("datatype.xml");
		HAPDataTypeImp dataType = (HAPDataTypeImp)HAPStringableEntityImporterXML.readRootEntity(stream, "data.datatypedef");
		dataType.resolveByConfigure(null);
		this.save(dataType);
	}

	private void save(HAPDataTypeImp dataType){
		try {
			HAPDataTypeInfoImp dataTypeInfo = (HAPDataTypeInfoImp)dataType.getDataTypeInfo();
			HAPDataTypeVersionImp dataTypeVersion = (HAPDataTypeVersionImp)dataTypeInfo.getVersion();
			HAPDataTypeInfoImp parentDataTypeInfo = (HAPDataTypeInfoImp)dataType.getParentDataTypeInfo();
			HAPDataTypeVersionImp linkedVersion = (HAPDataTypeVersionImp)dataType.getLinkedVersion();
			
			stmt.setString(1, dataTypeInfo.getStringValue());
			stmt.setString(2, dataTypeInfo.getName());
			stmt.setString(3, dataTypeVersion.getStringValue());
			stmt.setInt(4, dataTypeVersion.getMajor());
			stmt.setInt(5, dataTypeVersion.getMinor());
			stmt.setString(6, dataTypeVersion.getRevision());
			stmt.setString(7, dataType.getDescription());
			stmt.setString(8, parentDataTypeInfo==null?null:parentDataTypeInfo.getStringValue());
			stmt.setString(9, linkedVersion==null?null:linkedVersion.getStringValue());
			
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void close(){
		try {
			conn.close();
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
