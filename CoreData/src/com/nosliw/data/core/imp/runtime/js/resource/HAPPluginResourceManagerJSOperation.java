package com.nosliw.data.core.imp.runtime.js.resource;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.data.HAPOperation;
import com.nosliw.core.data.HAPOperationId;
import com.nosliw.core.data.HAPResourceIdOperation;
import com.nosliw.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.core.resource.HAPResourceHelper;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.imp.HAPDataAccessDataType;
import com.nosliw.data.core.imp.runtime.js.HAPDataAccessRuntimeJS;

public class HAPPluginResourceManagerJSOperation extends HAPPluginResourceManagerWithDataAccess{

	@HAPAttribute
	public static final String INFO_OPERATIONINFO = "operationInfo";
	
	private HAPDataAccessDataType m_dataTypeDataAccess = null;
	
	public HAPPluginResourceManagerJSOperation(HAPDataAccessRuntimeJS dataAccess, HAPDataAccessDataType dataTypeDataAccess){
		super(dataAccess);
		this.m_dataTypeDataAccess = dataTypeDataAccess;
		HAPResourceHelper.getInstance().registerResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_OPERATION, HAPResourceIdOperation.class, HAPOperationId.class);
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
