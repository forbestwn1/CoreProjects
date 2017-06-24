package com.nosliw.data.core.imp.runtime.js;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.data.core.HAPOperation;
import com.nosliw.data.core.imp.io.HAPDBAccess;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceIdOperation;
import com.nosliw.data.core.runtime.HAPResourceManager;

@HAPEntityWithAttribute
public class HAPResourceManagerJSOperation implements HAPResourceManager{

	public static final String INFO_OPERATIONINFO = "operationInfo";
	
	private HAPDBAccess m_dbAccess = HAPDBAccess.getInstance();
	
	@Override
	public List<HAPResource> getResources(List<HAPResourceId> resourcesId) {
		List<HAPResource> out = new ArrayList<HAPResource>();
		for(HAPResourceId resourceId : resourcesId){
			out.add(this.getResource(resourceId));
		}
		return out;
	}

	@Override
	public HAPResource getResource(HAPResourceId resourceId) {
		HAPResourceIdOperation resourceIdOperation = new HAPResourceIdOperation(resourceId);
		HAPResourceDataJSOperationImp helperResource = this.m_dbAccess.getJSOperation(resourceIdOperation.getOperationId());
		
		HAPOperation operationInfo = this.m_dbAccess.getOperationInfoByName(resourceIdOperation.getOperationId(), resourceIdOperation.getOperationId().getOperation());
		HAPInfoImpSimple info = new HAPInfoImpSimple(); 
		info.setValue(INFO_OPERATIONINFO, operationInfo);
		
		return new HAPResource(resourceId, helperResource, info);
	}
}
