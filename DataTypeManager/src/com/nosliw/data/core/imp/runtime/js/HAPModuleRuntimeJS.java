package com.nosliw.data.core.imp.runtime.js;

import org.springframework.stereotype.Component;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.data.HAPDataTypeConverter;
import com.nosliw.core.data.HAPJSHelperId;
import com.nosliw.core.data.HAPOperationId;
import com.nosliw.core.data.HAPResourceIdConverter;
import com.nosliw.core.data.HAPResourceIdJSHelper;
import com.nosliw.core.data.HAPResourceIdOperation;
import com.nosliw.core.gateway.HAPJSGatewayId;
import com.nosliw.core.gateway.HAPResourceIdJSGateway;
import com.nosliw.core.resource.HAPResourceHelper;
import com.nosliw.core.resource.imp.js.library.HAPJSLibraryId;
import com.nosliw.core.resource.imp.js.library.HAPResourceIdJSLibrary;
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

		//register resource type
		HAPResourceHelper resourceHelper = HAPResourceHelper.getInstance();
		resourceHelper.registerResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_OPERATION, HAPResourceIdOperation.class, HAPOperationId.class);
		resourceHelper.registerResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONVERTER, HAPResourceIdConverter.class, HAPDataTypeConverter.class);
		resourceHelper.registerResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSHELPER, HAPResourceIdJSHelper.class, HAPJSHelperId.class);
		resourceHelper.registerResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSLIBRARY, HAPResourceIdJSLibrary.class, HAPJSLibraryId.class);
		resourceHelper.registerResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSGATEWAY, HAPResourceIdJSGateway.class, HAPJSGatewayId.class);
		
		//data access
		this.m_runtimeJSDataAccess = new HAPDataAccessRuntimeJS(valueInfoManager, this.m_dataTypeModule.getDataAccess().getDBSource());
	}
	
	public HAPDataAccessDataType getDataTypeDataAccess(){  return this.m_dataTypeModule.getDataAccess();  }
	public HAPDataAccessRuntimeJS getRuntimeJSDataAccess(){  return this.m_runtimeJSDataAccess; }
}
