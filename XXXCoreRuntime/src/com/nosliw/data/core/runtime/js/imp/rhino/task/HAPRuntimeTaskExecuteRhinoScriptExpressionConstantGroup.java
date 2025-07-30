package com.nosliw.data.core.runtime.js.imp.rhino.task;

import java.util.List;

import com.nosliw.core.resource.imp.js.HAPJSScriptInfo;
import com.nosliw.core.runtime.js.rhino.HAPRuntimeTaskRhino;
import com.nosliw.core.runtime.js.rhino.task.HAPUtilityRuntimeJSScript;
import com.nosliw.core.runtimeenv.HAPRuntimeEnvironment;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup;

public class HAPRuntimeTaskExecuteRhinoScriptExpressionConstantGroup extends HAPRuntimeTaskRhino{

	final public static String TASK = "ExecuteScriptExpressionConstantGroup"; 

	private HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup m_scriptExpressionGroupInfo;
	
	public HAPRuntimeTaskExecuteRhinoScriptExpressionConstantGroup(HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup scriptExpressionGroupInfo, HAPRuntimeEnvironment runtTimeEnv) {
		super(TASK, runtTimeEnv);
		this.m_scriptExpressionGroupInfo = scriptExpressionGroupInfo;
	}

	@Override
	public Class getResultDataType() {
		return Object.class;
	}

	@Override
	protected List<HAPResourceDependency> getResourceDependency() {
		return null;
//		return this.m_taskInfo.getExpression().getResourceDependency(this.getRuntime().getRuntimeInfo(), this.getRuntimeEnv().getResourceManager());
	}

	@Override
	protected HAPJSScriptInfo buildRuntimeScript() {
		HAPJSScriptInfo scriptInfo = HAPUtilityRuntimeJSScript.buildTaskRequestScriptForExecuteExpressionScriptConstant(m_scriptExpressionGroupInfo, getTaskId(), getRuntime());
		return scriptInfo;
	}
}
