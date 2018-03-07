package com.nosliw.data.core.task.expression;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPProcessExpressionDefinitionContext;
import com.nosliw.data.core.operand.HAPOperandConstant;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.task.HAPExecutable;
import com.nosliw.data.core.task.HAPMatchers;
import com.nosliw.data.core.task.HAPProcessTaskContext;
import com.nosliw.data.core.task.HAPVariableInfo;

public class HAPProcessorStepExpression implements HAPProcessorStep{

	@Override
	public HAPExecuteStep process(HAPDefinitionStep stepDefinition,
			Map<String, HAPData> contextConstants,
			HAPProcessTaskContext context) {

		HAPDefinitionStepExpression stepDefExp = (HAPDefinitionStepExpression)stepDefinition;
		
		HAPExecuteStepExpression out = new HAPExecuteStepExpression(stepDefExp);
		
		this.processConstants(out.getOperand(), contextConstants);
		
		return out;
	}


	private void processConstants(HAPOperandWrapper operand, final Map<String, HAPData> contextConstants){
		HAPOperandUtility.processAllOperand(operand, null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_CONSTANT)){
					HAPOperandConstant constantOperand = (HAPOperandConstant)operand.getOperand();
					if(constantOperand.getData()==null){
						String constantName = constantOperand.getName();
						HAPData constantData = contextConstants.get(constantName);
						constantOperand.setData(constantData);
					}
				}
				return true;
			}
		});	
	}


	@Override
	public HAPMatchers discover(Map<String, HAPVariableInfo> parentVariablesInfo,
			HAPDataTypeCriteria expectOutputCriteria, HAPProcessExpressionDefinitionContext context) {
		// TODO Auto-generated method stub
		return null;
	}
}
