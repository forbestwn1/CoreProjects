package com.nosliw.data.core.task.expression;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandVariable;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.task.HAPExecuteTask;
import com.nosliw.data.core.task.HAPMatchers;
import com.nosliw.data.core.task.HAPProcessTaskContext;
import com.nosliw.data.core.task.HAPUpdateVariable;
import com.nosliw.data.core.task.HAPVariableInfo;

public class HAPExecuteStepExpression extends HAPExecuteStep{

	// original expression definition
	private HAPDefinitionStepExpression m_expressionDefinition;

	//Operand to represent the expression
	private HAPOperandWrapper m_operand;
	
	//output variable
	private String m_outputVariable;
	
	private boolean m_exits;
	
	public HAPExecuteStepExpression(HAPDefinitionStepExpression stepDef) {
		this.m_expressionDefinition = stepDef.clone();
		this.m_outputVariable = stepDef.getOutputVariable();
		this.m_exits = stepDef.isExit();
	}
	
	public Set<String> getReferenceNames(){   return this.m_expressionDefinition.getReferenceNames();    }
	
	public HAPOperandWrapper getOperand() {	return this.m_operand;	}

	@Override
	public void updateReferencedExecute(Map<String, HAPExecuteTask> references) {
		HAPOperandUtility.processAllOperand(this.m_operand, null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
					referenceOperand.updateReferenceExecute(references);
				}
				return true;
			}
		});	
	}

	@Override
	public void discover(
			Map<String, HAPVariableInfo> variablesInfo, 
			Map<String, HAPVariableInfo> localVariablesInfo, 
			Set<HAPDataTypeCriteria> exitCriterias, 
			HAPProcessTaskContext context,
			HAPDataTypeHelper dataTypeHelper){
		
		HAPMatchers matchers = null;
		//store all variables info including global and local, it is used for operand discovery
		Map<String, HAPVariableInfo> varsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		//only local variable
		Map<String, HAPVariableInfo> localVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		varsInfo.putAll(variablesInfo);
		varsInfo.putAll(localVariablesInfo);
		localVarsInfo.putAll(localVariablesInfo);

		//Do discovery until variables definition not change or fail 
		Map<String, HAPVariableInfo> oldVarsInfo;
		do{
			oldVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
			oldVarsInfo.putAll(varsInfo);
			
			context.clear();
			matchers = this.getOperand().getOperand().discover(varsInfo, null, context, dataTypeHelper);
		}while(!HAPBasicUtility.isEqualMaps(varsInfo, oldVarsInfo) && context.isSuccess());

		//if this step exit, then update exit criteria
		if(this.m_expressionDefinition.isExit()) {
			exitCriterias.add(this.getOutput());
		}
		
		//handle output variable
		String outVarName = this.m_outputVariable;
		if(outVarName!=null) {
			HAPVariableInfo localOutVarInfo = new HAPVariableInfo(this.getOperand().getOperand().getOutputCriteria());
			varsInfo.put(outVarName, localOutVarInfo);
			localVarsInfo.put(outVarName, localOutVarInfo);
		}

		//update variables in output
		variablesInfo.clear();
		localVariablesInfo.clear();
		for(String varName : varsInfo.keySet()) {
			HAPVariableInfo varInfo = variablesInfo.get(varName);
			if(localVarsInfo.get(varName)!=null) {
				//local var
				localVariablesInfo.put(varName, varInfo);
			}
			else {
				variablesInfo.put(varName, varInfo);
			}
		}
	}
	
	@Override
	public HAPDataTypeCriteria getOutput() {	return this.m_operand.getOperand().getOutputCriteria();	}

	public HAPExecuteStepExpression clone() {
		return null;
	}

	@Override
	public void updateVariable(HAPUpdateVariable updateVar) {
		if(this.m_outputVariable!=null) this.m_outputVariable = updateVar.getUpdatedVariable(this.m_outputVariable);
	
		//update variable operand
		HAPOperandUtility.processAllOperand(this.getOperand(), null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_VARIABLE)){
					HAPOperandVariable variableChild = (HAPOperandVariable)operand.getOperand();
					String newName = updateVar.getUpdatedVariable(variableChild.getVariableName()); 
					if(newName!=null)	variableChild.setVariableName(newName);
				}
				return true;
			}
		});	
	}

	@Override
	public List<HAPResourceId> discoverResources() {
		return this.m_operand.getOperand().getResources();
	}
}
