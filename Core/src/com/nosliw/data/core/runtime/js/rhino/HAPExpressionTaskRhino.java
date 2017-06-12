package com.nosliw.data.core.runtime.js.rhino;

import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.runtime.HAPExpressionTask;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSScriptUtility;

public abstract class HAPExpressionTaskRhino extends HAPExpressionTask{

	private HAPRuntimeImpJSRhino m_rhinoRuntime;
	
	public HAPExpressionTaskRhino(HAPExpression expression, Map<String, HAPData> variablesValue) {
		super(expression, variablesValue);
	}

	@Override
	abstract public void setResult(HAPData data);

	@Override
	public void resourceLoaded() {
		//after resource loaded, execute expression
		String script = HAPRuntimeJSScriptUtility.buildScriptForExecuteExpression(this);
		this.m_rhinoRuntime.loadTaskScript(script, this.getTaskId(), "execute");
		
	}

	public void setRuntime(HAPRuntimeImpJSRhino runtime){
		this.m_rhinoRuntime = runtime;
	}
}
