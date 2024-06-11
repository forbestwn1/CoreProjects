package com.nosliw.data.core.imp.runtime.js.resource;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.data.HAPOperation;
import com.nosliw.data.core.imp.HAPDataAccessDataType;
import com.nosliw.data.core.imp.runtime.js.HAPDataAccessRuntimeJS;
import com.nosliw.data.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPResourceIdOperation;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPPluginResourceManagerJSOperation extends HAPPluginResourceManagerWithDataAccess{

	@HAPAttribute
	public static final String INFO_OPERATIONINFO = "operationInfo";
	
	private HAPDataAccessDataType m_dataTypeDataAccess = null;
	
	public HAPPluginResourceManagerJSOperation(HAPDataAccessRuntimeJS dataAccess, HAPDataAccessDataType dataTypeDataAccess){
		super(dataAccess);
		this.m_dataTypeDataAccess = dataTypeDataAccess;
	}
	
	@Override
	public HAPResourceDataOrWrapper getResourceData(HAPResourceIdSimple simpleResourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdOperation resourceIdOperation = new HAPResourceIdOperation(simpleResourceId);
		HAPResourceDataJSOperationImp operationResource = this.getDataAccess().getJSOperation(resourceIdOperation.getOperationId());
		
		HAPOperation operationInfo = this.m_dataTypeDataAccess.getOperationInfoByName(resourceIdOperation.getOperationId(), resourceIdOperation.getOperationId().getOperation());
		operationResource.setOperationInfo(operationInfo);
		
		operationResource.setResourceDependency(this.getResourceDependency(simpleResourceId, runtimeInfo));
		return operationResource;
	}

}
