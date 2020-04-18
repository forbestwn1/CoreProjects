package com.nosliw.data.core.script.expression.expression;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.script.expression.HAPExecutableScript;

public class HAPExecutableScriptSegExpression implements HAPExecutableScript{

	private String m_id;
	
	private String m_expressionId;
	
	public HAPExecutableScriptSegExpression(String id, String expressionId) {
		this.m_id = id;
		this.m_expressionId = expressionId;
	}
	
	public String getExpressionId() {    return this.m_expressionId;    }

	@Override
	public String getId() {  return this.m_id;  }
	
	@Override
	public String getScriptType() {  return HAPConstant.SCRIPT_TYPE_SEG_EXPRESSION;  }

	@Override
	public Set<HAPVariableInfo> getVariablesInfo() {	return new HashSet<HAPVariableInfo>();	}

	@Override
	public Set<HAPDefinitionConstant> getConstantsDefinition() {	return new HashSet<HAPDefinitionConstant>();	}

	@Override
	public void updateConstant(Map<String, Object> value) {	}
	
}
