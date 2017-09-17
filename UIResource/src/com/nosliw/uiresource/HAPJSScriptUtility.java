package com.nosliw.uiresource;

import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;

public class HAPJSScriptUtility {

	public static HAPJSScriptInfo buildRequestScriptForExecuteScriptExpressionTask(HAPRuntimeTaskExecuteScriptExpression task){
		HAPScriptExpression scriptExpression = task.getScriptExpression();
		Map<String, HAPData> variableValue = task.getVariablesValue();
		
		return null;
	}
	
}
