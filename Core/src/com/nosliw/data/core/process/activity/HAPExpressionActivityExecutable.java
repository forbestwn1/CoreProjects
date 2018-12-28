package com.nosliw.data.core.process.activity;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPExecutableActivityNormal;
import com.nosliw.data.core.script.expression.HAPScriptExpression;

public class HAPExpressionActivityExecutable extends HAPExecutableActivityNormal{

	private Map<String, Object> m_constants;

	private HAPScriptExpression m_scriptExpression;

	public HAPExpressionActivityExecutable(String id, HAPDefinitionActivity activityDef) {
		super(id, activityDef);
		this.m_constants = new LinkedHashMap<String, Object>();
	}

	public void setConstants(Map<String, Object> constants) {
		this.m_constants.clear();
		this.m_constants.putAll(constants);   
	}
	
	public void setScriptExpression(HAPScriptExpression scriptExpression) {    this.m_scriptExpression = scriptExpression;    } 

}
