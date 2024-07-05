package com.nosliw.core.application.division.manual.common.dataexpression;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperand;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandAttribute;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandConstant;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandOperation;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandReference;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandVariable;

public class HAPManualUtilityOperand {

	private static HAPManualOperand buildManualOperand(HAPDefinitionOperand operandDef) {
		HAPManualOperand out = null;
		
		String operandType = operandDef.getType();
		
		if(operandType.equals(HAPConstantShared.EXPRESSION_OPERAND_ATTRIBUTEOPERATION)) {
			out = buildManualOperandAttribute((HAPDefinitionOperandAttribute)operandDef);
		}
		else if(operandType.equals(HAPConstantShared.EXPRESSION_OPERAND_CONSTANT)) {
			out = buildManualOperandConstant((HAPDefinitionOperandConstant)operandDef);
		}
		else if(operandType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)) {
			out = buildManualOperandVariable((HAPDefinitionOperandVariable)operandDef);
		}
		else if(operandType.equals(HAPConstantShared.EXPRESSION_OPERAND_OPERATION)) {
			out = buildManualOperandOperation((HAPDefinitionOperandOperation)operandDef);
		}
		else if(operandType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)) {
			out = buildManualOperandReference((HAPDefinitionOperandReference)operandDef);
		}
		
		return out;
	}
	
	private static HAPManualOperandAttribute buildManualOperandAttribute(HAPDefinitionOperandAttribute operandDef) {
		HAPManualOperandAttribute out = new HAPManualOperandAttribute(operandDef);
		out.setBase(buildManualOperand(operandDef.getBase()));
		return out;
	}
	
	private static HAPManualOperandConstant buildManualOperandConstant(HAPDefinitionOperandConstant operandDef) {
		HAPManualOperandConstant out = new HAPManualOperandConstant(operandDef);
		return out;
	}
	
	private static HAPManualOperandVariable buildManualOperandVariable(HAPDefinitionOperandVariable operandDef) {
		HAPManualOperandVariable out = new HAPManualOperandVariable(operandDef);
		return out;
	}

	private static HAPManualOperandOperation buildManualOperandOperation(HAPDefinitionOperandOperation operandDef) {
		HAPManualOperandOperation out = new HAPManualOperandOperation(operandDef);
		out.setBase(buildManualOperand(operandDef.getBase()));
		
		Map<String, HAPDefinitionOperand> parmsDef = operandDef.getParms();
		for(String parmName : parmsDef.keySet()) {
			out.setParm(parmName, buildManualOperand(parmsDef.get(parmName)));
		}
		return out;
	}
	
	private static HAPManualOperandReference buildManualOperandReference(HAPDefinitionOperandReference operandDef) {
		HAPManualOperandReference out = new HAPManualOperandReference(operandDef);
		return out;
	}
	
}
