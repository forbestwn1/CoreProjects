package com.nosliw.data.core.runtime.js.rhino.task;

import java.util.Map;

import com.nosliw.data.core.expression.HAPExecutableExpressionItem;
import com.nosliw.data.core.runtime.HAPRuntimeTask;

public abstract class HAPRuntimeTaskExecuteScriptExpressionAbstract extends HAPRuntimeTask{

	abstract public String getScriptFunction();	
	abstract public Map<String, HAPExecutableExpressionItem> getExpressionItems();
	abstract public Map<String, Object> getVariablesValue();
	abstract public Map<String, Object> getScriptConstants();
}
