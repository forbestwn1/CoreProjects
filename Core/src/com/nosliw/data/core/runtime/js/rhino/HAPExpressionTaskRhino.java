package com.nosliw.data.core.runtime.js.rhino;

import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.runtime.HAPExecuteExpressionTask;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSScriptUtility;

public abstract class HAPExpressionTaskRhino extends HAPExecuteExpressionTask{

	private HAPRuntimeImpRhino m_rhinoRuntime;
	
	public HAPExpressionTaskRhino(HAPExpression expression, Map<String, HAPData> variablesValue) {
		super(expression, variablesValue);
	}

	@Override
	protected void childSuccess(HAPRuntimeTask childTask){	
		//after resource loaded, execute expression
		HAPJSScriptInfo scriptInfo = HAPRuntimeJSScriptUtility.buildRequestScriptForExecuteExpressionTask(this);
		this.m_rhinoRuntime.loadTaskScript(scriptInfo, this.getTaskId());
		this.m_rhinoRuntime.removeTask(this.getTaskId());
	}

	
	public void setRuntime(HAPRuntimeImpRhino runtime){
		this.m_rhinoRuntime = runtime;
	}
}
