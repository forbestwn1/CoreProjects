package com.nosliw.uiresource.expression;

import java.util.Map;

import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.runtime.HAPExecuteExpression;
import com.nosliw.data.core.runtime.HAPRuntimeTask;

public abstract class HAPRuntimeTaskExecuteExpression extends HAPRuntimeTask{

	abstract public String getScriptFunction();	
	abstract public Map<String, HAPExecuteExpression> getExpressions();
	abstract public Map<String, Object> getVariablesValue();
	abstract public Map<String, Object> getScriptConstants();
}
