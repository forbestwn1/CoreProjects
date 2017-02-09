package com.nosliw.data.datatype.importer;

import java.sql.Connection;
import java.sql.DriverManager;

import com.nosliw.common.configure.HAPConfigurableImp;
import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.configure.HAPConfigureManager;
import com.nosliw.common.strvalue.valueinfo.HAPDBTableInfo;
import com.nosliw.common.strvalue.valueinfo.HAPSqlUtility;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;

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

	public void saveOperation(HAPOperationInfoImp operation, HAPDataTypeImp dataType){
		
		HAPSqlUtility.saveToDB(operation, m_connection);
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
