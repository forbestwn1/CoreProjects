package com.nosliw.core.runtime.js.rhino.task;

import java.util.List;

import com.nosliw.common.script.HAPJSScriptInfo;
import com.nosliw.core.resource.HAPResourceDependency;
import com.nosliw.core.runtime.js.rhino.HAPRuntimeImpRhino;
import com.nosliw.core.runtime.js.rhino.HAPRuntimeTaskRhino;

public class HAPRuntimeTaskExecuteRhinoScriptExpressionConstantGroup extends HAPRuntimeTaskRhino{

	final public static String TASK = "ExecuteScriptExpressionConstantGroup"; 

	private HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup m_scriptExpressionGroupInfo;
	
	public HAPRuntimeTaskExecuteRhinoScriptExpressionConstantGroup(HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup scriptExpressionGroupInfo) {
		super(TASK);
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
	protected HAPJSScriptInfo buildRuntimeScript(HAPRuntimeImpRhino runtime) {
		HAPJSScriptInfo scriptInfo = HAPUtilityRuntimeJSScript.buildTaskRequestScriptForExecuteExpressionScriptConstant(m_scriptExpressionGroupInfo, getTaskId(), runtime);
		return scriptInfo;
	}
}
