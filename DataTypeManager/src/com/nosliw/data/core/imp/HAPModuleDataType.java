package com.nosliw.data.core.imp;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.imp.io.HAPDBSource;

//runtime module for DataType
public class HAPModuleDataType {

	private HAPDataAccessDataType m_dataAccess;
	
	public HAPModuleDataType init(HAPValueInfoManager valueInfoManager){
		//value info
		valueInfoManager.importFromFolder(HAPFileUtility.getClassFolderPath(HAPDataAccessDataType.class), false);

		this.m_dataAccess = new HAPDataAccessDataType(valueInfoManager, HAPDBSource.getDefaultDBSource());
		
		return this;
	}
	
	public HAPDataAccessDataType getDataAccess(){  return this.m_dataAccess;  }
	
}
