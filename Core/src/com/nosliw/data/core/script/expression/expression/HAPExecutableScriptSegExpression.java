package com.nosliw.data.core.script.expression.expression;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.expression.HAPExecutableExpression;
import com.nosliw.data.core.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.script.expression.HAPExecutableScriptImp;

public class HAPExecutableScriptSegExpression extends HAPExecutableScriptImp{

	private String m_expressionId;
	
	public HAPExecutableScriptSegExpression(String id, String expressionId) {
		super(id);
		this.m_expressionId = expressionId;
	}
	
	public String getExpressionId() {    return this.m_expressionId;    }

	@Override
	public String getScriptType() {  return HAPConstant.SCRIPT_TYPE_SEG_EXPRESSION;  }

	@Override
	public Set<HAPVariableInfo> discoverVariablesInfo(HAPExecutableExpressionGroup expressionGroup) {
		HAPExecutableExpression expressionExe = expressionGroup.getExpressionItems().get(this.m_expressionId);
		return new HashSet<HAPVariableInfo>(expressionExe.getVariablesInfo().values());	
	}

	@Override
	public Set<HAPDefinitionConstant> discoverConstantsDefinition(HAPExecutableExpressionGroup expressionGroup) {
		HAPExecutableExpression expressionExe = expressionGroup.getExpressionItems().get(this.m_expressionId);
		return expressionExe.getConstantsDefinition();
	}

	@Override
	public Set<String> discoverExpressionReference(HAPExecutableExpressionGroup expressionGroup) {
		Set<String> out = new HashSet<String>();
		out.add(this.m_expressionId);
		return out;
	}
	
}
