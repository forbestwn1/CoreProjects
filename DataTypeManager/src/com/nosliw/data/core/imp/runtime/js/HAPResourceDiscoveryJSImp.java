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
	public List<HAPResourceId> discoverResourceRequirement(HAPDataTypeId dataTypeId, HAPOperation dataOpInfo) {
		HAPOperationId operationId = new HAPOperationId(dataTypeId, dataOpInfo.getName());
		HAPResourceId resourceId = new HAPResourceIdOperation(operationId);
		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
		resourceIds.add(resourceId);
		return this.discoverResourceDependency(resourceIds);
	}

	@Override
	public List<HAPResourceId> discoverResourceRequirement(HAPExpression expression) {
		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>(HAPExpressionUtility.discoverResources(expression));
		return this.discoverResourceDependency(resourceIds);
	}

	public List<HAPResourceId> discoverResourceDependency(List<HAPResourceId> resourceIds){
		List<HAPResourceId> out = new ArrayList<HAPResourceId>();
		for(HAPResourceId resourceId : resourceIds){
			this.discoverResourceDependency(resourceId, out);
		}
		return out;
	}

	private void discoverResourceDependency(HAPResourceId resourceId, List<HAPResourceId> resourceIds){
		resourceIds.add(resourceId);
		List<HAPResourceDependent> dependencys = this.getResourceDependency(resourceId);
		for(HAPResourceDependent dependencyId : dependencys){
			if(!resourceIds.contains(dependencyId)){
				this.discoverResourceDependency(resourceId, resourceIds);
			}
		}
	}
	
	public List<HAPResourceDependent> getResourceDependency(HAPResourceId resourceId){
		return this.m_dbAccess.getJSResourceDependency(resourceId).getDependency();
	}
}
