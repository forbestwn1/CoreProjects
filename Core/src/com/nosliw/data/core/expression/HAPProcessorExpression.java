package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;

public class HAPProcessorExpression {

	public static HAPExecutableExpression compileExpression(
			HAPDefinitionExpression expression, 
			Map<String, HAPDefinitionExpression> contextExpressionDefinitions, 
			Map<String, HAPVariableInfo> parentVariablesInfo, 
			Map<String, HAPData> contextConstants,
			HAPDataTypeCriteria expectOutput, 
			Map<String, String> configure,
			HAPProcessTracker processTracker) {
		
		HAPOperandWrapper operand = expression.getOperand().cloneWrapper();
		
		//expand referenced expression
		processReferencesInOperand(operand, contextExpressionDefinitions);
		
		HAPExecutableExpressionInSuite out = new HAPExecutableExpressionInSuite(operand.getOperand());
		
		//update constant data in expression
		HAPOperandUtility.updateConstantData(out.getOperand(), contextConstants);
		
		if(HAPExpressionProcessConfigureUtil.isDoDiscovery(configure)){
			//do discovery
			out.discover(parentVariablesInfo, expectOutput, processTracker);
		}
		
		return out;
	}

	
}
