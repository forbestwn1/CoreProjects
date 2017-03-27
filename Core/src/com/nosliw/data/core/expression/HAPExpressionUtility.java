package com.nosliw.data.core.expression;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPOperationId;

public class HAPExpressionUtility {

	static public void processAllOperand(HAPOperand operand, Object data, HAPExpressionTask task){
		if(task.processOperand(operand, data)){
			List<HAPOperand> children = operand.getChildren();
			for(HAPOperand child : children){
				HAPExpressionUtility.processAllOperand(child, data, task);
			}
			task.postPross(operand, data);
		}
	}
	
	static public Set<HAPOperationId> discoverOperations(HAPExpression expression){
		final Set<HAPOperationId> out = new HashSet<HAPOperationId>();
		
		processAllOperand(expression.getOperand(), out, new HAPExpressionTask(){
			@Override
			public boolean processOperand(HAPOperand operand, Object data) {
				if(operand.getType().equals(HAPConstant.EXPRESSION_OPERAND_OPERATION)){
					HAPOperationId operationId = ((HAPOperandOperation)operand).getOperationId();
					if(operationId!=null)  out.add(operationId);
				}
				return true;
			}

			@Override
			public void postPross(HAPOperand operand, Object data) {}
		});
		return out;
	}
	
}
