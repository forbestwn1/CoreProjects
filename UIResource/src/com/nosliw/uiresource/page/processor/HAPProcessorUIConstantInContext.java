package com.nosliw.uiresource.page.processor;

import java.util.Map;

import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.expression.HAPContextProcessExpressionScript;
import com.nosliw.uiresource.page.execute.HAPExecutableUIBody;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;

public class HAPProcessorUIConstantInContext {

	//resolve constants defined in context
	public static void resolveConstants(HAPExecutableUIUnit exeUnit, HAPRuntime runtime) {
		HAPExecutableUIBody body = exeUnit.getBody();
		HAPContextProcessExpressionScript scriptContext = body.getProcessExpressionScriptContext();
		Map<String, Object> constantsValue = body.getFlatContext().getConstantValue();
		for(String id : constantsValue.keySet()) {
			HAPDefinitionConstant constantDef = new HAPDefinitionConstant(id, constantsValue.get(id));
			scriptContext.addConstantDefinition(constantDef);
			if(constantDef.isData()) {
				body.getExpressionSuiteInContext().addConstantDefinition(constantDef);
			}
		}
		
		//expression
//		Map<String, HAPResourceDefinitionExpressionGroup> expressions = exeUnit.getExpressionContext().getExpressionDefinitions();
//		for(String name : expressions.keySet()) {
//			HAPOperandUtility.updateConstantData(expressions.get(name).getOperand(), HAPUtilityScriptExpression.getConstantData(constantsValue));
//		}
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : body.getUITags()) {
			resolveConstants(childTag, runtime);			
		}
		
	}
}
