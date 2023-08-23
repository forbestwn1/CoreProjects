package com.nosliw.data.core.script.expression1;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.domain.entity.expression.data.HAPExecutableEntityExpressionDataGroup;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.valuestructure1.HAPVariableInfoInStructure;

public abstract class HAPExecutableScriptImp extends HAPExecutableImp implements HAPExecutableScript{

	private String m_id;
	
	public HAPExecutableScriptImp(String id) {
		this.m_id = id;
	}

	@Override
	public String getId() {   return this.m_id;  }

	@Override
	public HAPVariableInfoInStructure discoverVariablesInfo1(HAPExecutableEntityExpressionDataGroup expressionGroup) {  return new HAPVariableInfoInStructure();  }

	@Override
	public Set<String> discoverVariables(HAPExecutableEntityExpressionDataGroup expressionGroup){  return new HashSet<String>();  }
	
	@Override
	public Set<HAPDefinitionConstant> discoverConstantsDefinition(HAPExecutableEntityExpressionDataGroup expressionGroup) {  return new HashSet<HAPDefinitionConstant>(); }
	
	@Override
	public void updateConstant(Map<String, Object> value) {	}

	@Override
	public Set<String> discoverExpressionReference(HAPExecutableEntityExpressionDataGroup expressionGroup) {  return new HashSet<String>(); }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SCRIPTTYPE, this.getScriptType());
		jsonMap.put(ID, this.m_id);
	}
}
