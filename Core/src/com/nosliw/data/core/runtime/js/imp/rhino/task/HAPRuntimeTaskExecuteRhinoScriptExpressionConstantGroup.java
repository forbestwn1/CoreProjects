package com.nosliw.data.core.runtime.js.imp.rhino.task;

import java.util.List;

import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.imp.rhino.HAPRuntimeTaskRhino;
import com.nosliw.data.core.runtime.js.util.script.HAPUtilityRuntimeJSScript;

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
