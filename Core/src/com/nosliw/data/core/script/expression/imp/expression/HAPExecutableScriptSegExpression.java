package com.nosliw.data.core.script.expression.imp.expression;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.expression.HAPExecutableExpression;
import com.nosliw.data.core.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.script.expression.HAPExecutableScriptImp;
import com.nosliw.data.core.structure.value.HAPContainerVariableCriteriaInfo;

public class HAPExecutableScriptSegExpression extends HAPExecutableScriptImp{

	private String m_expressionId;

	private HAPExecutableExpression m_expressionExe;

	public HAPExecutableScriptSegExpression(String id, String expressionId) {
		super(id);
		this.m_expressionId = expressionId;
	}
	
	public String getExpressionId() {    return this.m_expressionId;    }

	@Override
	public String getScriptType() {  return HAPConstantShared.SCRIPT_TYPE_SEG_EXPRESSION;  }

	@Override
	public HAPContainerVariableCriteriaInfo discoverVariablesInfo1(HAPExecutableExpressionGroup expressionGroup) {
		HAPExecutableExpression expressionExe = expressionGroup.getExpressionItems().get(this.m_expressionId);
		return expressionExe.getVariablesInfo();	
	}

	@Override
	public Set<String> discoverVariables(HAPExecutableExpressionGroup expressionGroup){
		if(m_expressionExe==null) {
			m_expressionExe = expressionGroup.getExpressionItems().get(this.m_expressionId);
		}
		return m_expressionExe.getVariablesInfo().getDataVariableNames();	
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
