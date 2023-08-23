package com.nosliw.data.core.script.expression1.resource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPUtilityRuntimeJS;
import com.nosliw.data.core.script.expression1.HAPExecutableScriptGroup;
import com.nosliw.data.core.script.expression1.HAPManagerScript;

public class HAPResourceManagerScript  extends HAPResourceManagerImp{

	private HAPManagerScript m_scriptMan;
	
	public HAPResourceManagerScript(HAPManagerScript expressionMan, HAPResourceManagerRoot rootResourceMan){
		super(rootResourceMan);
		this.m_scriptMan = expressionMan;
	}
	
	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPExecutableScriptGroup expression = this.m_scriptMan.getScript(resourceId, null);
		if(expression==null)  return null;
		Map<String, Object> info = new LinkedHashMap<String, Object>();
		info.put(HAPUtilityRuntimeJS.RESOURCE_LOADPATTERN, HAPUtilityRuntimeJS.RESOURCE_LOADPATTERN_FILE);
		return new HAPResource(resourceId, expression.toResourceData(runtimeInfo), null);
	}

	@Override
	protected List<HAPResourceDependency> getResourceDependency(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo){
		HAPExecutableScriptGroup expression = this.m_scriptMan.getScript(resourceId, null);
		return expression.getResourceDependency(runtimeInfo, this.m_rootResourceMan);
	}
}
