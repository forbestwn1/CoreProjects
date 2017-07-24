package com.nosliw.data.core.imp.runtime.js;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.data.core.HAPOperation;
import com.nosliw.data.core.imp.io.HAPDBAccess;
import com.nosliw.data.core.runtime.HAPLoadResourceResponse;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceIdOperation;
import com.nosliw.data.core.runtime.HAPResourceManager;

@HAPEntityWithAttribute
public class HAPResourceManagerJSOperation implements HAPResourceManager{

	@HAPAttribute
	public static final String INFO_OPERATIONINFO = "operationInfo";
	
	private HAPDBAccess m_dbAccess = HAPDBAccess.getInstance();
	
	@Override
	public HAPLoadResourceResponse getResources(List<HAPResourceId> resourcesId) {
		HAPLoadResourceResponse out = new HAPLoadResourceResponse();
		for(HAPResourceId resourceId : resourcesId){
			HAPResource resource = this.getResource(resourceId);
			if(resource!=null)  out.addLoadedResource(resource);
			else out.addFaildResourceId(resourceId);
		}
		return out;
	}

	@Override
	public HAPResource getResource(HAPResourceId resourceId) {
		HAPResourceIdOperation resourceIdOperation = new HAPResourceIdOperation(resourceId);
		HAPResourceDataJSOperationImp operationResource = this.m_dbAccess.getJSOperation(resourceIdOperation.getOperationId());
		if(operationResource==null)  return null;
		
		HAPOperation operationInfo = this.m_dbAccess.getOperationInfoByName(resourceIdOperation.getOperationId(), resourceIdOperation.getOperationId().getOperation());
		HAPInfoImpSimple info = new HAPInfoImpSimple(); 
		info.setValue(INFO_OPERATIONINFO, operationInfo);
		
		return new HAPResource(resourceId, operationResource, info);
	}
}
