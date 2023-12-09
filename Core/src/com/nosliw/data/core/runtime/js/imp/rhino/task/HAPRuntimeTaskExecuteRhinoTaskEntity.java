package com.nosliw.data.core.runtime.js.imp.rhino.task;

import java.util.List;

import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskTaskEntity;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.imp.rhino.HAPRuntimeTaskRhino;
import com.nosliw.data.core.runtime.js.util.script.HAPUtilityRuntimeJSScript;

public class HAPRuntimeTaskExecuteRhinoTaskEntity extends HAPRuntimeTaskRhino{
	final public static String TASK = "ExecuteTask"; 

	private HAPInfoRuntimeTaskTaskEntity m_taskInfo;
	
	public HAPRuntimeTaskExecuteRhinoTaskEntity(HAPInfoRuntimeTaskTaskEntity taskInfo, HAPRuntimeEnvironment runtTimeEnv) {
		super(TASK, runtTimeEnv);
		this.m_taskInfo = taskInfo;
	}

	@Override
	public Class getResultDataType() {	return m_taskInfo.getOutputClass();	}

	@Override
	protected List<HAPResourceDependency> getResourceDependency() {
		return null;
//		return this.m_taskInfo.getExpression().getResourceDependency(this.getRuntime().getRuntimeInfo(), this.getRuntimeEnv().getResourceManager());
	}

	@Override
	protected HAPJSScriptInfo buildRuntimeScript() {
		HAPJSScriptInfo scriptInfo = HAPUtilityRuntimeJSScript.buildTaskRequestScriptForExecuteTaskEntity(m_taskInfo.getBundle(), this.m_taskInfo.getMainEntityPath(), this.getTaskId(), getRuntime());
		return scriptInfo;
	}
}
