package com.nosliw.data.core.runtime.js.rhino.task;

import java.util.List;

import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskExpression;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeTaskRhino;
import com.nosliw.data.core.runtime.js.util.script.expression.HAPUtilityRuntimeJSScript;

public class HAPRuntimeTaskExecuteExpressionRhino extends HAPRuntimeTaskRhino{
	final public static String TASK = "ExecuteExpression"; 

	private HAPInfoRuntimeTaskExpression m_taskInfo;
	
	public HAPRuntimeTaskExecuteExpressionRhino(HAPInfoRuntimeTaskExpression taskInfo, HAPRuntimeEnvironment runtTimeEnv) {
		super(TASK, runtTimeEnv);
		this.m_taskInfo = taskInfo;
	}

	@Override
	public Class getResultDataType() {	return HAPData.class;	}

	@Override
	protected List<HAPResourceDependency> getResourceDependency() {
		return this.m_taskInfo.getExpression().getResourceDependency(this.getRuntime().getRuntimeInfo(), this.getRuntimeEnv().getResourceManager());
	}

	@Override
	protected HAPJSScriptInfo buildRuntimeScript() {
		HAPJSScriptInfo scriptInfo = HAPUtilityRuntimeJSScript.buildRequestScriptForExecuteExpressionTask(this.m_taskInfo, this, this.getRuntime());
		return scriptInfo;
	}
}
