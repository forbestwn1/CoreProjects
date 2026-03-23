package com.nosliw.data.core.imp.runtime.js;

import org.springframework.stereotype.Component;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.core.imp.HAPDataAccessDataType;
import com.nosliw.data.core.imp.HAPModuleDataType;

@Component
public class HAPModuleRuntimeJS {

	private HAPModuleDataType m_dataTypeModule;
	
	private HAPDataAccessRuntimeJS m_runtimeJSDataAccess;
	
	public HAPModuleRuntimeJS() {
		HAPValueInfoManager valueInfoManager = HAPValueInfoManager.getInstance(); 
		
		//init data type module
		m_dataTypeModule = new HAPModuleDataType();
		m_dataTypeModule.init(valueInfoManager);

		//value info
		valueInfoManager.importFromClassFolder(this.getClass());

		//data access
		this.m_runtimeJSDataAccess = new HAPDataAccessRuntimeJS(valueInfoManager, this.m_dataTypeModule.getDataAccess().getDBSource());
	}
	
	public HAPDataAccessDataType getDataTypeDataAccess(){  return this.m_dataTypeModule.getDataAccess();  }
	public HAPDataAccessRuntimeJS getRuntimeJSDataAccess(){  return this.m_runtimeJSDataAccess; }
}
