package com.nosliw.data.core.imp.runtime.js;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPOperation;
import com.nosliw.data.core.HAPOperationId;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceIdOperation;
import com.nosliw.data.core.runtime.HAPResourceInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDiscoveryJS;

public class HAPResourceDiscoveryJSImp extends HAPResourceDiscoveryJS{

	private HAPDataAccessRuntimeJS m_dataAccess;
	
	public HAPResourceDiscoveryJSImp(HAPDataAccessRuntimeJS dataAccess){
		this.m_dataAccess = dataAccess;
		this.init();
	}

	private void init(){}
	
	@Override
	public List<HAPResourceInfo> discoverResourceRequirement(HAPDataTypeId dataTypeId, HAPOperation dataOpInfo) {
		HAPOperationId operationId = new HAPOperationId(dataTypeId, dataOpInfo.getName());
		HAPResourceId resourceId = new HAPResourceIdOperation(operationId);
		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
		resourceIds.add(resourceId);
		return this.discoverResource(resourceIds);
	}

	@Override
	public List<HAPResourceInfo> discoverResourceRequirement(List<HAPExpression> expressions) {
		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
		for(HAPExpression expression : expressions){
			resourceIds.addAll(HAPExpressionUtility.discoverResources(expression));
		}
		return this.discoverResource(new ArrayList<HAPResourceId>(resourceIds));
	}

	@Override
	public List<HAPResourceInfo> discoverResource(List<HAPResourceId> resourceIds){
		List<HAPResourceInfo> out = new ArrayList<HAPResourceInfo>();
		Set<HAPResourceId> exisitingResources = new HashSet<HAPResourceId>();
		for(HAPResourceId resourceId : resourceIds){
			this.discoverResource(resourceId, out, exisitingResources);
		}
		return out;
	}

	private void discoverResource(HAPResourceId resourceId, List<HAPResourceInfo> resourceInfos, Set<HAPResourceId> exisitingResources){
		if(!exisitingResources.contains(resourceId)){
			HAPResourceInfo resourceInfo = new HAPResourceInfo(resourceId);
			exisitingResources.add(resourceId);
			//add dependency first
			List<HAPResourceDependent> dependencys = this.getResourceDependency(resourceId);
			for(HAPResourceDependent dependency : dependencys){
				resourceInfo.addDependency(dependency);
				if(!resourceInfos.contains(dependency.getId())){
					this.discoverResource(dependency.getId(), resourceInfos, exisitingResources);
				}
			}
			//then add itself
			resourceInfos.add(resourceInfo);
		}
	}
	
	public List<HAPResourceDependent> getResourceDependency(HAPResourceId resourceId){
		List<HAPResourceDependent> out = new ArrayList<HAPResourceDependent>();
		HAPJSResourceDependency dependency = this.m_dataAccess.getJSResourceDependency(resourceId);
		if(dependency!=null)  out = dependency.getDependency();
		return out;
	}
}
