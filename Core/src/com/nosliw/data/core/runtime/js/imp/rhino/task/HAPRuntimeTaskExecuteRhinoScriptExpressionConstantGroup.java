package com.nosliw.data.core.runtime.js.imp.rhino.task;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.container.HAPContainer;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.scriptexpression.HAPExpressionScript;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskTaskScriptExpressionConstantItem;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.imp.rhino.HAPRuntimeTaskRhino;
import com.nosliw.data.core.runtime.js.util.script.HAPUtilityRuntimeJSScript;

public class HAPRuntimeTaskExecuteRhinoScriptExpressionConstantGroup extends HAPRuntimeTaskRhino{
	final public static String TASK = "ExecuteScriptExpressionConstant"; 

	private HAPContainer<HAPInfoRuntimeTaskTaskScriptExpressionConstantItem> m_container;
	
	private HAPExpressionScript m_expressionScript;
	
	private Map<String, Object> m_constants = new LinkedHashMap<String, Object>();
	
	public HAPRuntimeTaskExecuteRhinoScriptExpressionConstantGroup(HAPExpressionScript expressionScript, Map<String, Object> constants, HAPRuntimeEnvironment runtTimeEnv) {
		super(TASK, runtTimeEnv);
		this.m_expressionScript = expressionScript;
		if(constants!=null) {
			this.m_constants.putAll(constants);
		}
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
		HAPJSScriptInfo scriptInfo = HAPUtilityRuntimeJSScript.buildTaskRequestScriptForExecuteExpressionScriptConstant(m_expressionScript, m_constants, getTaskId(), getRuntime());
		return scriptInfo;
	}
}
