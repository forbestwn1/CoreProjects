package com.nosliw.data.core.imp.resource.js;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPOperation;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.imp.HAPDataTypeIdImp;
import com.nosliw.data.core.imp.HAPOperationIdImp;
import com.nosliw.data.core.imp.io.HAPDBAccess;
import com.nosliw.data.core.imp.resource.HAPResourceIdImp;
import com.nosliw.data.core.imp.resource.HAPResourceImp;
import com.nosliw.data.core.imp.resource.js.io.HAPJSImporter;
import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceManager;
import com.nosliw.data.core.resource.HAPRuntimeInfo;

public class HAPResourceManagerImpJS implements HAPResourceManager{

	private static HAPResourceManagerImpJS m_instance;
	
	private HAPDBAccess m_dbAccess;
	
	public static HAPResourceManagerImpJS getInstance(){
		if(m_instance==null){
			m_instance = new HAPResourceManagerImpJS();
		}
		return m_instance;
	}
	
	private void init(){
		this.m_dbAccess = HAPDBAccess.getInstance();
		
		HAPValueInfoManager.getInstance().importFromXML(HAPJSImporter.class, new String[]{
				"jsoperation.xml",
				"jsresourcedependency.xml"
		});
	}
	
	private HAPResourceManagerImpJS(){
		this.m_dbAccess = HAPDBAccess.getInstance();
		this.init();
	}
	
	public HAPDBAccess getDBAccess(){		return this.m_dbAccess;	}
	
	@Override
	public HAPRuntimeInfo getRuntimeInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HAPResourceId> discoverResourceRequirement(HAPDataTypeId dataTypeId, HAPOperation dataOpInfo) {
		HAPOperationIdImp operationId = new HAPOperationIdImp(dataTypeId, dataOpInfo.getName());
		HAPResourceIdImp resourceId = new HAPResourceIdImp(
				HAPConstant.DATAOPERATION_RESOURCE_TYPE_DATATYPEOPERATION, 
				operationId.toStringValue(HAPSerializationFormat.LITERATE));
		Set<HAPResourceIdImp> resourceIds = new HashSet<HAPResourceIdImp>();
		resourceIds.add(resourceId);
		return this.discoverResourceDependency(resourceIds);
	}

	@Override
	public Set<HAPResourceId> discoverResourceRequirement(HAPExpression expression) {
		
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HAPResource> getResources(Set<HAPResourceId> resourcesId) {
		Set<HAPResource> out = new HashSet<HAPResource>();
		for(HAPResourceId resourceId : resourcesId){
			out.add(this.getResourceById(resourceId));
		}
		return out;
	}

	public Set<HAPResourceId> discoverResourceDependency(Set<HAPResourceIdImp> resourceIds){
		Set<HAPResourceId> out = new HashSet<HAPResourceId>();
		for(HAPResourceIdImp resourceId : resourceIds){
			this.discoverResourceDependency(resourceId, out);
		}
		return out;
	}

	private void discoverResourceDependency(HAPResourceIdImp resourceId, Set<HAPResourceId> resourceIds){
		resourceIds.add(resourceId);
		Set<HAPResourceId> dependencys = this.getResourceDependency(resourceId);
		for(HAPResourceId dependencyId : dependencys){
			if(!resourceIds.contains(dependencyId)){
				this.discoverResourceDependency(resourceId, resourceIds);
			}
		}
	}
	
	public Set<HAPResourceId> getResourceDependency(HAPResourceIdImp resourceId){
		return new HashSet(this.m_dbAccess.getJSResourceDependency(resourceId));
	}
	
	
	public HAPResource getResourceById(HAPResourceId resourceId){
		HAPResource out = null;
		String resourceType = resourceId.getType();
		switch(resourceType)
		{
		case HAPConstant.DATAOPERATION_RESOURCE_TYPE_DATATYPEOPERATION:
			HAPOperationIdImp operationId = (HAPOperationIdImp)HAPSerializeManager.getInstance().buildObject(resourceId.getId(), HAPOperationIdImp.class, HAPSerializationFormat.LITERATE);
			HAPJSOperation jsOperation = this.m_dbAccess.getJSOperation(operationId);
			out = new HAPResourceImp(resourceId, null, jsOperation);
			break;
		}
		
		return out;
	}
}
