package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSUtility;

public class HAPResourceManagerExpression  extends HAPResourceManagerImp{

	private HAPManagerExpression m_expressionMan;
	
	public HAPResourceManagerExpression(HAPManagerExpression expressionMan, HAPResourceManagerRoot rootResourceMan){
		super(rootResourceMan);
		this.m_expressionMan = expressionMan;
	}
	
	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPExecutableExpression expression = this.m_expressionMan.getExpression(resourceId, null, null);
		if(expression==null)  return null;
		Map<String, Object> info = new LinkedHashMap<String, Object>();
		info.put(HAPRuntimeJSUtility.RESOURCE_LOADPATTERN, HAPRuntimeJSUtility.RESOURCE_LOADPATTERN_FILE);
		return new HAPResource(resourceId, expression.toResourceData(runtimeInfo), null);
	}

	@Override
	protected List<HAPResourceDependency> getResourceDependency(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo){
		HAPExecutableExpression expression = this.m_expressionMan.getExpression(resourceId, null, null);
		return expression.getResourceDependency(runtimeInfo, this.m_rootResourceMan);
	}
}
