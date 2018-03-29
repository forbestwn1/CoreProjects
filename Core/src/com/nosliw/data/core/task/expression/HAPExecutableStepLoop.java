package com.nosliw.data.core.task.expression;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.operand.HAPOperand;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.task.HAPExecutableTask;
import com.nosliw.data.core.task.HAPUpdateVariable;

public class HAPExecutableStepLoop extends HAPExecutableStep{

	private HAPOperandWrapper m_containerOperand;
	
	private String m_elementVariable;
	
	private HAPOperandWrapper m_executeOperand;

	Map<String, HAPVariableInfo> m_variablesInfo;
	
	Set<String> m_references;

	public HAPExecutableStepLoop(HAPDefinitionStepLoop loopStepDef, int index, String name) {
		super(index, name);
		this.m_containerOperand = loopStepDef.getContainer().getOperand().cloneWrapper();
		this.m_elementVariable = loopStepDef.getElementVariable();
		this.m_executeOperand = loopStepDef.getExecuteTask().getOperand().cloneWrapper();
		this.m_references = loopStepDef.getReferenceNames();
		this.m_variablesInfo = new LinkedHashMap<String, HAPVariableInfo>();
	}

	public HAPOperandWrapper getContainerOperand() {  return this.m_containerOperand;   }
	public HAPOperandWrapper getExecuteOperand() {   return this.m_executeOperand;   }
	public String getElementVariable() {  return this.m_elementVariable;   }
	
	@Override
	public String getType() {  return HAPConstant.EXPRESSIONTASK_STEPTYPE_EXPRESSION;	}

	@Override
	public HAPDataTypeCriteria getOutput() {  return this.m_containerOperand.getOperand().getOutputCriteria();  }

	@Override
	public void updateVariable(HAPUpdateVariable updateVar) {
		HAPOperandUtility.updateVariable(this.m_containerOperand, updateVar);
	}

	@Override
	public void discoverVariable(Map<String, HAPVariableInfo> variablesInfo, HAPDataTypeCriteria expectOutputCriteria,
			HAPProcessContext context) {
		Map<String, HAPVariableInfo> varsInfo = HAPOperandUtility.discover(new HAPOperand[] {this.m_containerOperand.getOperand(), this.m_executeOperand.getOperand()}, variablesInfo, expectOutputCriteria, context);
		variablesInfo.clear();
		variablesInfo.putAll(varsInfo);
		m_variablesInfo.clear();
		m_variablesInfo.putAll(varsInfo);
	}

	@Override
	public List<HAPResourceId> getResourceDependency() {		return this.m_containerOperand.getOperand().getResources();	}

	@Override
	public Set<String> getReferences() {  return this.m_references;  }

	@Override
	public Set<String> getVariables() {  return this.m_variablesInfo.keySet();  }

	@Override
	public HAPDataTypeCriteria getExitDataTypeCriteria() {	return null;	}

	@Override
	public void updateReferencedExecute(Map<String, HAPExecutableTask> references) {
		HAPOperandUtility.updateReferencedExecute(this.m_containerOperand, references);
		HAPOperandUtility.updateReferencedExecute(this.m_executeOperand, references);
	}
}
