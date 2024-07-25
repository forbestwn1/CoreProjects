package com.nosliw.data.core.runtime.js.imp.rhino.task;

import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.scriptexpression.HAPExpressionScript;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.imp.rhino.HAPRuntimeTaskRhino;
import com.nosliw.data.core.runtime.js.util.script.HAPUtilityRuntimeJSScript;

public class HAPRuntimeTaskExecuteRhinoScriptExpressionConstant extends HAPRuntimeTaskRhino{
	final public static String TASK = "ExecuteScriptExpressionConstant"; 

	private HAPExpressionScript m_expressionScript;
	
	public HAPRuntimeTaskExecuteRhinoScriptExpressionConstant(HAPExpressionScript expressionScript, HAPRuntimeEnvironment runtTimeEnv) {
		super(TASK, runtTimeEnv);
		this.m_expressionScript = expressionScript;
	}

	@Override
	public Class getResultDataType() {
		String scriptType = this.m_expressionScript.getType();
		if(scriptType.equals(HAPConstantShared.EXPRESSION_TYPE_LITERATE)) {
			return String.class;
		} else if(scriptType.equals(HAPConstantShared.EXPRESSION_TYPE_TEXT)) {
			return String.class;
		} else if(scriptType.equals(HAPConstantShared.EXPRESSION_TYPE_SCRIPT)) {
			return Object.class;
		}
		return Object.class;
	}

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
