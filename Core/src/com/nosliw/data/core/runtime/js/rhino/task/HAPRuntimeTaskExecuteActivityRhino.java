package com.nosliw.data.core.runtime.js.rhino.task;

import java.util.List;

import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskActivity;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeTaskRhino;
import com.nosliw.data.core.runtime.js.util.script.activity.HAPUtilityRuntimeJSScriptActivity;

public class HAPRuntimeTaskExecuteActivityRhino extends HAPRuntimeTaskRhino{
	
	final public static String TASK = "ExecuteActivity"; 

	private HAPInfoRuntimeTaskActivity m_taskInfo;
	
	public HAPRuntimeTaskExecuteActivityRhino(HAPInfoRuntimeTaskActivity taskInfo, HAPRuntimeEnvironment runtTimeEnv) {
		super(TASK, runtTimeEnv);
		this.m_taskInfo = taskInfo;
	}

	@Override
	protected List<HAPResourceDependency> getResourceDependency() {
		return this.m_taskInfo.getActivitySuiite().getResourceDependency(this.getRuntime().getRuntimeInfo(), this.getRuntimeEnv().getResourceManager());
	}

	@Override
	protected HAPJSScriptInfo buildRuntimeScript() {
		HAPJSScriptInfo scriptInfo = HAPUtilityRuntimeJSScriptActivity.buildRequestScript(this.m_taskInfo, this, this.getRuntime());
		return scriptInfo;
	}

}
