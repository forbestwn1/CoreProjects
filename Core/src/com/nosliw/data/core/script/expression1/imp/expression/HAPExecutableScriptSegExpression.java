package com.nosliw.data.core.script.expression1.imp.expression;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.application.common.dataexpression.HAPExecutableExpressionData1;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.domain.entity.expression.data1.HAPExecutableEntityExpressionDataGroup;
import com.nosliw.data.core.script.expression1.HAPExecutableScriptImp;
import com.nosliw.data.core.valuestructure1.HAPVariableInfoInStructure;

public class HAPExecutableScriptSegExpression extends HAPExecutableScriptImp{

	private String m_expressionId;

	private HAPExecutableExpressionData1 m_expressionExe;

	public HAPExecutableScriptSegExpression(String id, String expressionId) {
		super(id);
		this.m_expressionId = expressionId;
	}
	
	public String getExpressionId() {    return this.m_expressionId;    }

	@Override
	public String getScriptType() {  return HAPConstantShared.EXPRESSION_SEG_TYPE_DATA;  }

	@Override
	public HAPVariableInfoInStructure discoverVariablesInfo1(HAPExecutableEntityExpressionDataGroup expressionGroup) {
//		HAPExecutableExpression expressionExe = expressionGroup.getExpressionItems().get(this.m_expressionId);
//		return expressionExe.getVariablesInfo());
		return null;
	}

	@Override
	public Set<String> discoverVariables(HAPExecutableEntityExpressionDataGroup expressionGroup){
		if(m_expressionExe==null) {
			m_expressionExe = expressionGroup.getAllExpressionItems().get(this.m_expressionId);
		}
		return m_expressionExe.getVariablesInfo().getVariablesId();	
	}

	@Override
	public Set<HAPDefinitionConstant> discoverConstantsDefinition(HAPExecutableEntityExpressionDataGroup expressionGroup) {
		HAPExecutableExpressionData1 expressionExe = expressionGroup.getAllExpressionItems().get(this.m_expressionId);
		return expressionExe.getConstantsDefinition();
	}

	@Override
	public Set<String> discoverExpressionReference(HAPExecutableEntityExpressionDataGroup expressionGroup) {
		Set<String> out = new HashSet<String>();
		out.add(this.m_expressionId);
		return out;
	}

	@Override
	public void updateVariableName(HAPUpdateName nameUpdate) {	}
	
}
