package com.nosliw.data.core.runtime.js.imp.rhino.task;

import java.util.List;

import com.nosliw.core.resource.imp.js.HAPJSScriptInfo;
import com.nosliw.core.runtime.js.rhino.HAPRuntimeTaskRhino;
import com.nosliw.core.runtimeenv.HAPRuntimeEnvironment;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskTask1;
import com.nosliw.data.core.runtime.js.util.script.task.HAPUtilityRuntimeJSScriptTask;

public class HAPRuntimeTaskExecuteTaskRhino extends HAPRuntimeTaskRhino{
	
	final public static String TASK = "ExecuteActivity"; 

	private HAPInfoRuntimeTaskTask1 m_taskInfo;
	
	public HAPRuntimeTaskExecuteTaskRhino(HAPInfoRuntimeTaskTask1 taskInfo, HAPRuntimeEnvironment runtTimeEnv) {
		super(TASK, runtTimeEnv);
		this.m_taskInfo = taskInfo;
	}

	@Override
	protected List<HAPResourceDependency> getResourceDependency() {
		return this.m_taskInfo.getTaskSuite().getResourceDependency(this.getRuntime().getRuntimeInfo(), this.getRuntimeEnv().getResourceManager());
	}

	@Override
	protected HAPJSScriptInfo buildRuntimeScript() {
		HAPJSScriptInfo scriptInfo = HAPUtilityRuntimeJSScriptTask.buildRequestScript(this.m_taskInfo, this, this.getRuntime());
		return scriptInfo;
	}

}
