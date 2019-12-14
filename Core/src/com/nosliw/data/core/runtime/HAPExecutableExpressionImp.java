package com.nosliw.data.core.runtime;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;

//entity that can is runnable within runtime environment
public abstract class HAPExecutableExpressionImp extends HAPSerializableImp implements HAPExecutableExpression{

	List<HAPResourceDependency> m_resources;
	
	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		if(this.m_resources==null) {
			List<HAPResourceId> expressionDependency = HAPExpressionUtility.discoverResources(this);
			this.m_resources = new ArrayList<HAPResourceDependency>();
			for(HAPResourceId id : expressionDependency) {
				this.m_resources.add(new HAPResourceDependency(id));
			}
		}
		return this.m_resources;
	}

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		// TODO Auto-generated method stub
		return null;
	}
}
