package com.nosliw.data.core.runtime;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.entity.expression.data.HAPExecutableExpressionData;
import com.nosliw.data.core.runtime.js.util.script.expressionscrip2.HAPScriptFunctionInfo;
import com.nosliw.data.core.runtime.js.util.script.expressionscrip2.HAPUtilityScriptForExecuteJSScript;
import com.nosliw.data.core.script.expression1.HAPExecutableScriptGroup;

public class HAPInfoRuntimeTaskScriptExpression2{

	@HAPAttribute
	public static String SCRIPTEXPRESSION = "scriptExpression";

	@HAPAttribute
	public static String VARIABLESVALUE = "variablesValue";
	
	private HAPExecutableScriptGroup m_scriptGroup;
	
	private Object m_scriptId;
	
	//variable value can be data or other object
	Map<String, Object> m_variablesValue;
	
	Map<String, Object> m_scriptConstants;
	
	public HAPInfoRuntimeTaskScriptExpression2(
			HAPExecutableScriptGroup scriptGroup, 
			Object scriptId,
			Map<String, Object> variablesValue, 
			Map<String, Object> scriptConstants){
		this.m_scriptGroup = scriptGroup;
		this.m_scriptId = scriptId;
		this.m_variablesValue = variablesValue; 
		this.m_scriptConstants = scriptConstants!=null?scriptConstants:new LinkedHashMap<String, Object>();
	}
	
	public HAPExecutableScriptGroup getScriptGroup() {   return this.m_scriptGroup;   }
	public HAPScriptFunctionInfo getScriptFunction() {
		return HAPUtilityScriptForExecuteJSScript.buildFunctionInfo(this.m_scriptGroup.getScript(m_scriptId));
	}
	public Map<String, HAPExecutableExpressionData> getExpressionItems(){  return this.m_scriptGroup.getExpression().getAllExpressionItems(); }
	public Map<String, Object> getVariablesValue(){  return this.m_variablesValue;  }
	public Map<String, Object> getConstantsValue(){  return new LinkedHashMap<String, Object>();  }

}
