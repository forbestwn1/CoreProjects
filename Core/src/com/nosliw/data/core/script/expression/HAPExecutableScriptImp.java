package com.nosliw.data.core.script.expression;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.runtime.HAPExecutableImp;

public abstract class HAPExecutableScriptImp extends HAPExecutableImp implements HAPExecutableScript{

	private String m_id;
	
	public HAPExecutableScriptImp(String id) {
		this.m_id = id;
	}

	@Override
	public String getId() {   return this.m_id;  }

	@Override
	public Set<HAPVariableInfo> discoverVariablesInfo(HAPExecutableExpressionGroup expressionGroup) {  return new HashSet<HAPVariableInfo>();  }

	@Override
	public Set<HAPDefinitionConstant> discoverConstantsDefinition(HAPExecutableExpressionGroup expressionGroup) {  return new HashSet<HAPDefinitionConstant>(); }
	
	@Override
	public void updateConstant(Map<String, Object> value) {	}

	@Override
	public Set<String> discoverExpressionReference(HAPExecutableExpressionGroup expressionGroup) {  return new HashSet<String>(); }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SCRIPTTYPE, this.getScriptType());
		jsonMap.put(ID, this.m_id);
	}
}
