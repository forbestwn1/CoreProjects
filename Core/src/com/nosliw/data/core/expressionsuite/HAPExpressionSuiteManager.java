package com.nosliw.data.core.expressionsuite;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.operand.HAPOperand;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.runtime.HAPExecuteExpression;

public class HAPExpressionSuiteManager {

	private HAPDataTypeHelper m_dataTypeHelper;
	
	public HAPExpressionSuiteManager(HAPDataTypeHelper dataTypeHelper) {
		this.m_dataTypeHelper = dataTypeHelper;
	}
	
	public HAPExecuteExpression compileExpression(
			String id,
			HAPDefinitionExpression expression, 
			Map<String, HAPDefinitionExpression> contextExpressionDefinitions, 
			Map<String, HAPVariableInfo> parentVariablesInfo, 
			Map<String, HAPData> contextConstants,
			HAPDataTypeCriteria expectOutput, 
			Map<String, String> configure) {
		
		return HAPExpressionSuiteUtility.compileExpression(id, expression, contextExpressionDefinitions, parentVariablesInfo, contextConstants, expectOutput, configure, m_dataTypeHelper);
	}

	public HAPExecuteExpression compileExpression(
			String id,
			HAPDefinitionExpression expression, 
			Map<String, HAPVariableInfo> parentVariablesInfo, 
			Map<String, HAPData> contextConstants,
			HAPDataTypeCriteria expectOutput, 
			Map<String, String> configure) {
		
		return HAPExpressionSuiteUtility.compileExpression(id, expression, new LinkedHashMap<String, HAPDefinitionExpression>(), parentVariablesInfo, contextConstants, expectOutput, configure, m_dataTypeHelper);
	}


	public HAPExecuteExpression compileExpression(
			String id,
			HAPDefinitionExpression expression, 
			HAPDefinitionExpressionSuite contextExpressionDefinitionsSuite, 
			HAPDataTypeCriteria expectOutput, 
			Map<String, String> configure) {
		return this.compileExpression(id, expression, contextExpressionDefinitionsSuite.getExpressionDefinitions(), contextExpressionDefinitionsSuite.getVariablesInfo(), contextExpressionDefinitionsSuite.getConstants(), expectOutput, configure);
	}	
	
	//replace reference operand with referenced operand
	private void processReferencesInOperand(HAPOperandWrapper operand, 
			Map<String, HAPDefinitionExpression> contextExpressionDefinitions) {
		HAPOperandUtility.processAllOperand(operand, null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
					HAPDefinitionExpression refedExpDef = contextExpressionDefinitions.get(referenceOperand.getReferenceName());
					HAPOperand refedOperand = refedExpDef.getOperand().getOperand().cloneOperand();
					operand.setOperand(refedOperand);
					processReferencesInOperand(operand, contextExpressionDefinitions);
				}
				return true;
			}
		});	
	}
	
	
}
