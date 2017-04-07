package com.nosliw.data.core.imp.runtime.js;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPOperation;
import com.nosliw.data.core.HAPOperationId;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.imp.io.HAPDBAccess;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.js.HAPResourceIdOperation;
import com.nosliw.data.core.runtime.js.HAPResourceDiscoveryJS;

public class HAPResourceDiscoveryJSImp extends HAPResourceDiscoveryJS{

	private static HAPResourceDiscoveryJSImp m_instance;
	
	private HAPDBAccess m_dbAccess;
	
	public static HAPResourceDiscoveryJSImp getInstance(){
		if(m_instance==null){
			m_instance = new HAPResourceDiscoveryJSImp();
		}
		return m_instance;
	}
	
	private void init(){
		this.m_dbAccess = HAPDBAccess.getInstance();
		
		HAPValueInfoManager.getInstance().importFromXML(HAPResourceDiscoveryJSImp.class, new String[]{
				"jsoperation.xml",
				"jsresourcedependency.xml",
				"jshelper.xml"
		});
	}
	
	private HAPResourceDiscoveryJSImp(){
		this.m_dbAccess = HAPDBAccess.getInstance();
		this.init();
	}
	
	public HAPDBAccess getDBAccess(){		return this.m_dbAccess;	}
	
	@Override
	public List<HAPResourceId> discoverResourceRequirement(HAPDataTypeId dataTypeId, HAPOperation dataOpInfo) {
		HAPOperationId operationId = new HAPOperationId(dataTypeId, dataOpInfo.getName());
		HAPResourceId resourceId = new HAPResourceIdOperation(operationId, null);
		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
		resourceIds.add(resourceId);
		return this.discoverResourceDependency(resourceIds);
	}

	@Override
	public List<HAPResourceId> discoverResourceRequirement(HAPExpression expression) {
		
		
		return null;
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
		Set<HAPResourceId> dependencys = this.getResourceDependency(resourceId);
		for(HAPResourceId dependencyId : dependencys){
			if(!resourceIds.contains(dependencyId)){
				this.discoverResourceDependency(resourceId, resourceIds);
			}
		}
	}
	
	public Set<HAPResourceId> getResourceDependency(HAPResourceId resourceId){
		return new HashSet(this.m_dbAccess.getJSResourceDependency(resourceId));
	}
}
