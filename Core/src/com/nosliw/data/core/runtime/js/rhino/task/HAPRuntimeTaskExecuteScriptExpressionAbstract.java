package com.nosliw.data.core.runtime.js.rhino.task;

import java.util.Map;

import com.nosliw.data.core.expression.HAPExecutableExpression;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.js.util.script.HAPScriptFunctionInfo;

public abstract class HAPRuntimeTaskExecuteScriptExpressionAbstract extends HAPRuntimeTask{

	abstract public HAPScriptFunctionInfo getScriptFunction();	
	abstract public Map<String, HAPExecutableExpression> getExpressionItems();
	abstract public Map<String, Object> getVariablesValue();
	abstract public Map<String, Object> getScriptConstants();
}
