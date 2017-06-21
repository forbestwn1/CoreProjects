package com.nosliw.data.core.imp.runtime.js;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPOperation;
import com.nosliw.data.core.HAPOperationId;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.imp.io.HAPDBAccess;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceIdOperation;
import com.nosliw.data.core.runtime.HAPResourceInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDiscoveryJS;

public class HAPResourceDiscoveryJSImp extends HAPResourceDiscoveryJS{

	private HAPDBAccess m_dbAccess;
	
	public HAPResourceDiscoveryJSImp(){
		this.m_dbAccess = HAPDBAccess.getInstance();
		this.init();
	}

	private void init(){
	}
	
	public HAPDBAccess getDBAccess(){		return this.m_dbAccess;	}
	
	@Override
	public List<HAPResourceInfo> discoverResourceRequirement(HAPDataTypeId dataTypeId, HAPOperation dataOpInfo) {
		HAPOperationId operationId = new HAPOperationId(dataTypeId, dataOpInfo.getName());
		HAPResourceId resourceId = new HAPResourceIdOperation(operationId);
		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
		resourceIds.add(resourceId);
		return this.discoverResource(resourceIds);
	}

	@Override
	public List<HAPResourceInfo> discoverResourceRequirement(HAPExpression expression) {
		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>(HAPExpressionUtility.discoverResources(expression));
		return this.discoverResource(resourceIds);
	}

	@Override
	public List<HAPResourceInfo> discoverResource(List<HAPResourceId> resourceIds){
		List<HAPResourceInfo> out = new ArrayList<HAPResourceInfo>();
		for(HAPResourceId resourceId : resourceIds){
			this.discoverResource(resourceId, out);
		}
		return out;
	}

	private void discoverResource(HAPResourceId resourceId, List<HAPResourceInfo> resourceInfos){
		HAPResourceInfo resourceInfo = new HAPResourceInfo(resourceId);
		resourceInfos.add(resourceInfo);
		
		List<HAPResourceDependent> dependencys = this.getResourceDependency(resourceId);
		for(HAPResourceDependent dependency : dependencys){
			resourceInfo.addDependency(dependency);
			if(!resourceInfos.contains(dependency.getId())){
				this.discoverResource(dependency.getId(), resourceInfos);
			}
		}
	}
	
	public List<HAPResourceDependent> getResourceDependency(HAPResourceId resourceId){
		List<HAPResourceDependent> out = new ArrayList<HAPResourceDependent>();
		HAPJSResourceDependency dependency = this.m_dbAccess.getJSResourceDependency(resourceId);
		if(dependency!=null)  out = dependency.getDependency();
		return out;
	}
}
