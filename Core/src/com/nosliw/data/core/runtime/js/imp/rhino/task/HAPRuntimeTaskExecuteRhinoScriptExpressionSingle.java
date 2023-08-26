package com.nosliw.data.core.runtime.js.imp.rhino.task;

import java.util.List;

import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskScriptExpressionSingle;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.imp.rhino.HAPRuntimeTaskRhino;
import com.nosliw.data.core.runtime.js.util.script.expressionscrip.HAPUtilityRuntimeJSScript;

public class HAPRuntimeTaskExecuteRhinoScriptExpressionSingle extends HAPRuntimeTaskRhino{

	final public static String TASK = "ExecuteScriptExpression"; 
	
	private HAPInfoRuntimeTaskScriptExpressionSingle m_taskInfo;
	
	public HAPRuntimeTaskExecuteRhinoScriptExpressionSingle(HAPInfoRuntimeTaskScriptExpressionSingle taskInfo, HAPRuntimeEnvironment runtTimeEnv) {
		super(TASK, runtTimeEnv);
		this.m_taskInfo = taskInfo;
	}

	@Override
	protected List<HAPResourceDependency> getResourceDependency() {
		return null;
//		return this.m_taskInfo.getScriptGroup().getResourceDependency(this.getRuntime().getRuntimeInfo(), this.getRuntimeEnv().getResourceManager());
	}

	@Override
	protected HAPJSScriptInfo buildRuntimeScript() {
		HAPJSScriptInfo scriptInfo = HAPUtilityRuntimeJSScript.buildRequestScriptForExecuteExpressionSingleTask(this.m_taskInfo, this, this.getRuntime());
		return scriptInfo;
	}
	
}
