package com.nosliw.data.core.imp.runtime.js;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPDataTypeConverter;
import com.nosliw.data.core.HAPOperationId;
import com.nosliw.data.core.imp.HAPDataAccessDataType;
import com.nosliw.data.core.imp.HAPModuleDataType;
import com.nosliw.data.core.runtime.HAPResourceHelper;
import com.nosliw.data.core.runtime.HAPResourceIdConverter;
import com.nosliw.data.core.runtime.HAPResourceIdOperation;
import com.nosliw.data.core.runtime.js.HAPJSHelperId;
import com.nosliw.data.core.runtime.js.HAPJSLibraryId;
import com.nosliw.data.core.runtime.js.HAPResourceIdJSHelper;
import com.nosliw.data.core.runtime.js.HAPResourceIdJSLibrary;

public class HAPModuleRuntimeJS {

	private HAPModuleDataType m_dataTypeModule;
	
	private HAPDataAccessRuntimeJS m_runtimeJSDataAccess;
	
	public HAPModuleRuntimeJS init(HAPValueInfoManager valueInfoManager){
		//init data type module
		m_dataTypeModule = new HAPModuleDataType();
		m_dataTypeModule.init(valueInfoManager);

		//value info
		HAPValueInfoManager.getInstance().importFromFolder(HAPFileUtility.getClassFolderPath(HAPResourceDiscoveryJSImp.class), false);

		//register resource type
		HAPResourceHelper resourceHelper = HAPResourceHelper.getInstance();
		resourceHelper.registerResourceId(HAPConstant.RUNTIME_RESOURCE_TYPE_OPERATION, HAPResourceIdOperation.class, HAPOperationId.class);
		resourceHelper.registerResourceId(HAPConstant.RUNTIME_RESOURCE_TYPE_CONVERTER, HAPResourceIdConverter.class, HAPDataTypeConverter.class);
		resourceHelper.registerResourceId(HAPConstant.RUNTIME_RESOURCE_TYPE_JSHELPER, HAPResourceIdJSHelper.class, HAPJSHelperId.class);
		resourceHelper.registerResourceId(HAPConstant.RUNTIME_RESOURCE_TYPE_JSLIBRARY, HAPResourceIdJSLibrary.class, HAPJSLibraryId.class);
		
		//data access
		this.m_runtimeJSDataAccess = new HAPDataAccessRuntimeJS(valueInfoManager, this.m_dataTypeModule.getDataAccess().getDBSource());

		return this;
	}
	
	public HAPDataAccessDataType getDataTypeDataAccess(){  return this.m_dataTypeModule.getDataAccess();  }
	public HAPDataAccessRuntimeJS getRuntimeJSDataAccess(){  return this.m_runtimeJSDataAccess; }
}
