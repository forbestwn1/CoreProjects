package com.nosliw.data.core.process.activity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.operand.HAPOperand;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.task111.HAPExecutableTask;

public class HAPExecutableStepBranch{
/*
extends HAPExecutableStep{

	private HAPOperandWrapper m_expression;
	
	private HAPResultStep m_trueResult;

	private HAPResultStep m_falseResult;
	
	private Map<String, HAPVariableInfo> m_variablesInfo;
	
	public HAPExecutableStepBranch(HAPDefinitionStepBranch branchStepDef, int index, String name) {
		super(index, name);
		this.m_expression = branchStepDef.getExpression().getOperand().cloneWrapper();
		HAPOperandUtility.replaceAttributeOpWithOperationOp(this.m_expression);
		
		this.m_trueResult = branchStepDef.getTrueResult();
		this.m_falseResult = branchStepDef.getFalseResult();
		this.m_variablesInfo = new LinkedHashMap<String, HAPVariableInfo>();
	}

	public HAPResultStep getTrueResult() {   return this.m_trueResult;  }
	public HAPResultStep getFalseResult() {   return this.m_falseResult;  }
	public HAPOperandWrapper getExpression() {  return this.m_expression;   }
	
	@Override
	public String getType() {  return HAPConstant.EXPRESSIONTASK_STEPTYPE_BRANCH;  }

	@Override
	public HAPDataTypeCriteria getOutput() {   return null;  }

	@Override
	public void updateVariable(HAPUpdateName updateVar) {
		HAPOperandUtility.updateVariableName(this.m_expression, updateVar);
		this.m_trueResult.updateVariable(updateVar);
		this.m_falseResult.updateVariable(updateVar);
	}

	@Override
	public void discoverVariable(Map<String, HAPVariableInfo> variablesInfo, HAPDataTypeCriteria expectOutputCriteria,
			HAPProcessContext context) {
		Map<String, HAPVariableInfo> varsInfo = HAPOperandUtility.discover(new HAPOperand[] {this.m_expression.getOperand()}, variablesInfo, expectOutputCriteria, context);
		
		variablesInfo.clear();
		variablesInfo.putAll(varsInfo);
		m_variablesInfo.clear();
		m_variablesInfo.putAll(varsInfo);
	}

	@Override
	public List<HAPResourceId> getResourceDependency() {		return this.m_expression.getOperand().getResources();	}

	@Override
	public Set<String> getReferences() {  return HAPOperandUtility.discoverReferences(this.m_expression);  }

	@Override
	public Set<String> getVariables() {  return this.m_variablesInfo.keySet();  }

	@Override
	public HAPDataTypeCriteria getExitDataTypeCriteria() {  return null; }

	@Override
	public void updateReferencedExecute(Map<String, HAPExecutableTask> references) {
		HAPOperandUtility.updateReferencedExecute(this.m_expression, references);
	}
*/	
}
