package com.nosliw.data.core.runtime;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.expression.HAPExpressionUtility;

//entity that can is runnable within runtime environment
public abstract class HAPExecutableExpressionImp extends HAPSerializableImp implements HAPExecutableExpression{

	List<HAPResourceDependent> m_resources;
	
	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		if(this.m_resources==null) {
			List<HAPResourceId> expressionDependency = HAPExpressionUtility.discoverResources(this);
			this.m_resources = new ArrayList<HAPResourceDependent>();
			for(HAPResourceId id : expressionDependency) {
				this.m_resources.add(new HAPResourceDependent(id));
			}
		}
		return this.m_resources;
	}

}
