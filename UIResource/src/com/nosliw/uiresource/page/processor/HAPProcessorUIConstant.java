package com.nosliw.uiresource.page.processor;

import java.util.Map;

import com.nosliw.data.core.expression.HAPResourceDefinitionExpressionGroup;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.expression.HAPUtilityScriptExpression;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;

public class HAPProcessorUIConstant {

	//resolve the name 
	public static void resolveConstants(HAPExecutableUIUnit exeUnit, HAPRuntime runtime) {
		Map<String, Object> constantsValue = exeUnit.getFlatContext().getConstantValue();
		
		//expression
		Map<String, HAPResourceDefinitionExpressionGroup> expressions = exeUnit.getExpressionContext().getExpressionDefinitions();
		for(String name : expressions.keySet()) {
			HAPOperandUtility.updateConstantData(expressions.get(name).getOperand(), HAPUtilityScriptExpression.getConstantData(constantsValue));
		}
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : exeUnit.getUITags()) {
			resolveConstants(childTag, runtime);			
		}
		
	}
}
