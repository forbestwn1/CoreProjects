package com.nosliw.data.core.runtime.js.rhino.task;

import java.util.List;

import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeTaskRhino;
import com.nosliw.data.core.runtime.js.util.script.expressionscrip.HAPUtilityScriptForExecuteJSScript;

public class HAPRuntimeTaskExecuteScript extends HAPRuntimeTaskRhino{

	final public static String TASK = "ExecuteScriptExpression"; 
	
	private HAPInfoRuntimeTaskScript m_taskInfo;
	
	public HAPRuntimeTaskExecuteScript(HAPInfoRuntimeTaskScript taskInfo, HAPRuntimeEnvironment runtTimeEnv) {
		super(TASK, runtTimeEnv);
		this.m_taskInfo = taskInfo;
	}

	@Override
	protected List<HAPResourceDependency> getResourceDependency() {
		return this.m_taskInfo.getScriptGroup().getResourceDependency(this.getRuntime().getRuntimeInfo(), this.getRuntimeEnv().getResourceManager());
	}

	@Override
	protected HAPJSScriptInfo buildRuntimeScript() {
		HAPJSScriptInfo scriptInfo = HAPUtilityScriptForExecuteJSScript.buildRequestScriptForExecuteScriptTask(this.m_taskInfo, this, this.getRuntime());
		return scriptInfo;
	}
	
}
