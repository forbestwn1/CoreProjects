package com.nosliw.data.core.script.expression.imp.expression;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.domain.entity.expression.HAPExecutableExpression;
import com.nosliw.data.core.domain.entity.expression.HAPExecutableEntityExpressionGroup;
import com.nosliw.data.core.script.expression.HAPExecutableScriptImp;
import com.nosliw.data.core.valuestructure1.HAPVariableInfoInStructure;

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
	public HAPVariableInfoInStructure discoverVariablesInfo1(HAPExecutableEntityExpressionGroup expressionGroup) {
//		HAPExecutableExpression expressionExe = expressionGroup.getExpressionItems().get(this.m_expressionId);
//		return expressionExe.getVariablesInfo());
		return null;
	}

	@Override
	public Set<String> discoverVariables(HAPExecutableEntityExpressionGroup expressionGroup){
		if(m_expressionExe==null) {
			m_expressionExe = expressionGroup.getExpressionItems().get(this.m_expressionId);
		}
		return m_expressionExe.getVariablesInfo().getVariablesId();	
	}

	@Override
	public Set<HAPDefinitionConstant> discoverConstantsDefinition(HAPExecutableEntityExpressionGroup expressionGroup) {
		HAPExecutableExpression expressionExe = expressionGroup.getExpressionItems().get(this.m_expressionId);
		return expressionExe.getConstantsDefinition();
	}

	@Override
	public Set<String> discoverExpressionReference(HAPExecutableEntityExpressionGroup expressionGroup) {
		Set<String> out = new HashSet<String>();
		out.add(this.m_expressionId);
		return out;
	}

	@Override
	public void updateVariableName(HAPUpdateName nameUpdate) {	}
	
}
