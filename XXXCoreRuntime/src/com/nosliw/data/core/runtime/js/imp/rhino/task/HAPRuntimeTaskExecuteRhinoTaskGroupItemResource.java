package com.nosliw.data.core.runtime.js.imp.rhino.task;

import java.util.List;

import com.nosliw.core.resource.imp.js.HAPJSScriptInfo;
import com.nosliw.core.runtime.js.rhino.HAPRuntimeTaskRhino;
import com.nosliw.core.runtime.js.rhino.task.HAPUtilityRuntimeJSScript;
import com.nosliw.core.runtimeenv.HAPRuntimeEnvironment;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskTaskGroupItemResource;

public class HAPRuntimeTaskExecuteRhinoTaskGroupItemResource extends HAPRuntimeTaskRhino{
	final public static String TASK = "ExecuteTaskGroupItem"; 

	private HAPInfoRuntimeTaskTaskGroupItemResource m_taskInfo;
	
	public HAPRuntimeTaskExecuteRhinoTaskGroupItemResource(HAPInfoRuntimeTaskTaskGroupItemResource taskInfo, HAPRuntimeEnvironment runtTimeEnv) {
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
		HAPJSScriptInfo scriptInfo = HAPUtilityRuntimeJSScript.buildTaskRequestScriptForExecuteTaskGroupItemResource(m_taskInfo.getResourceType(), m_taskInfo.getResourceId(), m_taskInfo.getItemName(), this.getTaskId(), getRuntime());
		return scriptInfo;
	}
}
