package com.nosliw.data.datatype.importer.js;

import java.util.Set;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPOperation;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceManager;
import com.nosliw.data.core.resource.HAPRuntimeInfo;
import com.nosliw.data.datatype.importer.HAPDBAccess;
import com.nosliw.data.datatype.importer.HAPDataTypeIdImp;
import com.nosliw.data.datatype.importer.HAPResourceImp;

public class HAPResourceManagerImpJS implements HAPResourceManager{

	private static HAPResourceManagerImpJS m_instance;
	
	private HAPDBAccess m_dataAccess;
	
	public static HAPResourceManagerImpJS getInstance(){
		if(m_instance==null){
			m_instance = new HAPResourceManagerImpJS();
		}
		return m_instance;
	}
	
	private HAPResourceManagerImpJS(){
		this.m_dataAccess = HAPDBAccess.getInstance();
	}
	
	@Override
	public HAPRuntimeInfo getRuntimeInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HAPResourceId> discoverResourceRequirement(HAPDataTypeId dataTypeInfo, HAPOperation dataOpInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HAPResourceId> discoverResourceRequirement(HAPExpression expression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HAPResource> getResources(Set<HAPResourceId> resourcesId) {
		
		
		return null;
	}

	
	public HAPResource getResourceById(HAPResourceId resourceId){
		HAPResource out = null;
		String resourceType = resourceId.getType();
		switch(resourceType)
		{
		case HAPConstant.DATAOPERATION_RESOURCE_TYPE_DATATYPEOPERATION:
			String[] segs = HAPNamingConversionUtility.parseSegments(resourceId.getId());
			HAPJSOperation jsOperation = this.m_dataAccess.getJSOperation(new HAPDataTypeIdImp(segs[0], segs[1]), segs[2]);
			out = new HAPResourceImp(resourceId, null, jsOperation, jsOperation.getDependency());
			break;
		}
		
		return out;
	}
	
	
}
