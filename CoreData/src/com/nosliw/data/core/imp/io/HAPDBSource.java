package com.nosliw.data.core.imp.io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.nosliw.common.configure.HAPConfigurableImp;
import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.configure.HAPConfigureManager;

public class HAPDBSource  extends HAPConfigurableImp{

	private Connection m_connection;
	
	private static HAPDBSource defaultDBSource;
	
	public static HAPDBSource getDefaultDBSource(){
		if(defaultDBSource==null){
			defaultDBSource = new HAPDBSource();
		}
		return defaultDBSource;
	}
	
	private HAPDBSource() {
		HAPConfigureImp configure = (HAPConfigureImp) HAPConfigureManager.getInstance().createConfigure()
				.cloneChildConfigure("dataTypeManager.database");
		this.setConfiguration(configure);

		setupDbConnection();
	}

	private void setupDbConnection() {
		try {
			Class.forName(this.getConfigureValue("jdbc.driver").getStringContent());
			m_connection = DriverManager.getConnection(this.getConfigureValue("jdbc.url").getStringContent(),
					this.getConfigureValue("username").getStringContent(),
					this.getConfigureValue("password").getStringContent());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection(){
		try {
			if(this.m_connection.isClosed()) {
				this.setupDbConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.m_connection;
	}
	
	public void destroy() {
		try {
			this.m_connection.abort(null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
