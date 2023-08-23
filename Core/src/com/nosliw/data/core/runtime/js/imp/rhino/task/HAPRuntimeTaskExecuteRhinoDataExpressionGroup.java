package com.nosliw.data.core.runtime.js.imp.rhino.task;

import java.util.List;

import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskDataExpressionGroup;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.imp.rhino.HAPRuntimeTaskRhino;
import com.nosliw.data.core.runtime.js.util.script.expressiondata.HAPUtilityRuntimeJSScript;

public class HAPRuntimeTaskExecuteRhinoDataExpressionGroup extends HAPRuntimeTaskRhino{
	final public static String TASK = "ExecuteExpression"; 

	private HAPInfoRuntimeTaskDataExpressionGroup m_taskInfo;
	
	public HAPRuntimeTaskExecuteRhinoDataExpressionGroup(HAPInfoRuntimeTaskDataExpressionGroup taskInfo, HAPRuntimeEnvironment runtTimeEnv) {
		super(TASK, runtTimeEnv);
		this.m_taskInfo = taskInfo;
	}

	@Override
	public Class getResultDataType() {	return HAPData.class;	}

	@Override
	protected List<HAPResourceDependency> getResourceDependency() {
		return null;
		
//		return this.m_taskInfo.getExpression().getResourceDependency(this.getRuntime().getRuntimeInfo(), this.getRuntimeEnv().getResourceManager());
	}

	@Override
	protected HAPJSScriptInfo buildRuntimeScript() {
		HAPJSScriptInfo scriptInfo = HAPUtilityRuntimeJSScript.buildRequestScriptForExecuteDataExpressionGroupTask(this.m_taskInfo, this, this.getRuntime());
		return scriptInfo;
	}
}
