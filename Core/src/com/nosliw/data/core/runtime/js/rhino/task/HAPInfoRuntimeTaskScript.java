package com.nosliw.data.core.runtime.js.rhino.task;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.expression.HAPExecutableExpression;
import com.nosliw.data.core.runtime.js.util.script.expressionscrip.HAPScriptFunctionInfo;
import com.nosliw.data.core.runtime.js.util.script.expressionscrip.HAPUtilityScriptForExecuteJSScript;
import com.nosliw.data.core.script.expression.HAPExecutableScriptGroup;

public class HAPInfoRuntimeTaskScript{

	@HAPAttribute
	public static String SCRIPTEXPRESSION = "scriptExpression";

	@HAPAttribute
	public static String VARIABLESVALUE = "variablesValue";
	
	private HAPExecutableScriptGroup m_scriptGroup;
	
	private Object m_scriptId;
	
	//variable value can be data or other object
	Map<String, Object> m_variablesValue;
	
	Map<String, Object> m_scriptConstants;
	
	public HAPInfoRuntimeTaskScript(
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
	public Map<String, HAPExecutableExpression> getExpressionItems(){  return this.m_scriptGroup.getExpression().getExpressionItems(); }
	public Map<String, Object> getVariablesValue(){  return this.m_variablesValue;  }
	public Map<String, Object> getConstantsValue(){  return new LinkedHashMap<String, Object>();  }

}
