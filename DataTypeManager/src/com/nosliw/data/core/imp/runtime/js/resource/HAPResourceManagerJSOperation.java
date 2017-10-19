package com.nosliw.data.core.imp.runtime.js.resource;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.data.core.HAPOperation;
import com.nosliw.data.core.imp.HAPDataAccessDataType;
import com.nosliw.data.core.imp.runtime.js.HAPDataAccessRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerDataAccess;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceIdOperation;

@HAPEntityWithAttribute
public class HAPResourceManagerJSOperation extends HAPResourceManagerDataAccess{

	@HAPAttribute
	public static final String INFO_OPERATIONINFO = "operationInfo";
	
	private HAPDataAccessDataType m_dataTypeDataAccess = null;
	
	public HAPResourceManagerJSOperation(HAPDataAccessRuntimeJS dataAccess, HAPDataAccessDataType dataTypeDataAccess){
		super(dataAccess);
		this.m_dataTypeDataAccess = dataTypeDataAccess;
	}
	
	@Override
	public HAPResource getResource(HAPResourceId resourceId) {
		HAPResourceIdOperation resourceIdOperation = new HAPResourceIdOperation(resourceId);
		HAPResourceDataJSOperationImp operationResource = this.getDataAccess().getJSOperation(resourceIdOperation.getOperationId());
		if(operationResource==null)  return null;
		
		HAPOperation operationInfo = this.m_dataTypeDataAccess.getOperationInfoByName(resourceIdOperation.getOperationId(), resourceIdOperation.getOperationId().getOperation());
		HAPInfoImpSimple info = new HAPInfoImpSimple(); 
		info.setValue(INFO_OPERATIONINFO, operationInfo);
		
		return new HAPResource(resourceId, operationResource, info);
	}
}
