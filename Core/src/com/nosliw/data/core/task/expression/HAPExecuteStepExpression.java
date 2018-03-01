package com.nosliw.data.core.task.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.expression.HAPProcessExpressionDefinitionContext;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.task.HAPExecutable;
import com.nosliw.data.core.task.HAPUpdateVariable;

public class HAPExecuteStepExpression extends HAPExecuteStep{

	// original expression definition
	private HAPDefinitionStepExpression m_expressionDefinition;

	//Operand to represent the expression
	private HAPOperandWrapper m_operand;
	
	// store all the matchers from variables info to internal variables info in expression
	// it convert variable from caller to variable in expression
	private Map<String, HAPMatchers> m_varsMatchers;
	
	
	
	public HAPExecuteStepExpression(HAPDefinitionStepExpression stepDef) {
		this.m_expressionDefinition = stepDef.clone();
	}
	
	public HAPOperandWrapper getOperand() {	return this.m_operand;	}


	public HAPMatchers discover(Map<String, HAPVariableInfo> parentVariablesInfo, HAPDataTypeCriteria expectOutputCriteria, HAPProcessExpressionDefinitionContext context,	HAPDataTypeHelper dataTypeHelper){
		this.m_varsInfo = parentVariablesInfo;
		if(this.m_varsInfo==null)   this.m_varsInfo = new LinkedHashMap<String, HAPVariableInfo>();      
		
		//update variables info in expression according to parent variable info (parent variable info affect the child variable info)
		for(String varName : this.m_localVarsInfo.keySet()){
			HAPVariableInfo varInfo = this.m_localVarsInfo.get(varName);
			HAPVariableInfo parentVarInfo = this.m_varsInfo.get(varName);
			if(parentVarInfo!=null){
				if(varInfo.getStatus().equals(HAPConstant.EXPRESSION_VARIABLE_STATUS_OPEN)){
					HAPDataTypeCriteria adjustedCriteria = dataTypeHelper.merge(varInfo.getCriteria(), parentVarInfo.getCriteria());
					varInfo.setCriteria(adjustedCriteria);
				}
			}
		}
		
		//do discovery on operand
		Map<String, HAPVariableInfo> varsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		varsInfo.putAll(this.m_localVarsInfo);
		
		HAPMatchers matchers = null;
		Map<String, HAPVariableInfo> oldVarsInfo;
		//Do discovery until local vars definition not change or fail 
		do{
			oldVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
			oldVarsInfo.putAll(varsInfo);
			
			context.clear();
			matchers = this.getOperand().getOperand().discover(varsInfo, expectOutputCriteria, context, dataTypeHelper);
		}while(!HAPBasicUtility.isEqualMaps(varsInfo, oldVarsInfo) && context.isSuccess());
		this.m_localVarsInfo = varsInfo;
		
		//merge back, cal variable matchers, update parent variable
		for(String varName : this.m_localVarsInfo.keySet()){
			HAPVariableInfo varInfo = this.m_localVarsInfo.get(varName);
			HAPVariableInfo parentVarInfo = this.m_varsInfo.get(varName);
			if(parentVarInfo==null){
				parentVarInfo = new HAPVariableInfo();
				parentVarInfo.setCriteria(varInfo.getCriteria());
				this.m_varsInfo.put(varName, parentVarInfo);
			}
			else{
				if(parentVarInfo.getStatus().equals(HAPConstant.EXPRESSION_VARIABLE_STATUS_OPEN)){
					HAPDataTypeCriteria adjustedCriteria = dataTypeHelper.merge(varInfo.getCriteria(), parentVarInfo.getCriteria());
					parentVarInfo.setCriteria(adjustedCriteria);
				}
			}

			//cal var converters
			HAPMatchers varMatchers = dataTypeHelper.buildMatchers(parentVarInfo.getCriteria(), varInfo.getCriteria());
			this.m_varsMatchers.put(varName, varMatchers);
		}
		
		return matchers;
	}
	
	
	
	
	@Override
	public HAPDataTypeCriteria getOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	public HAPExecuteStepExpression clone() {
		return null;
	}

	@Override
	public void updateVariable(HAPUpdateVariable updateVar) {
		// TODO Auto-generated method stub
		
	}
	
	
}
