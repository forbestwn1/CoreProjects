package com.nosliw.data.core.imp.io;

import java.sql.Connection;

import com.nosliw.common.strvalue.HAPStringableValueEntityWithID;
import com.nosliw.common.strvalue.valueinfo.HAPDBAccess;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;

public class HAPDataAccess extends HAPDBAccess{

	private HAPDBSource m_dbSource;
	

	public HAPDataAccess(HAPValueInfoManager valueInfoMan, HAPDBSource dbSource) {
		super(valueInfoMan);
		this.m_dbSource = dbSource;
	}

	
	public void createDBTable(String dataTypeName) {
		this.createDBTable(dataTypeName, getConnection());
	}
	
	public HAPStringableValueEntityWithID saveEntity(HAPStringableValueEntityWithID entity){
		return this.saveEntity(entity, getConnection());
	}
	
	protected Connection getConnection(){		return this.m_dbSource.getConnection();	}

	public HAPDBSource getDBSource(){  return this.m_dbSource;  }
}
