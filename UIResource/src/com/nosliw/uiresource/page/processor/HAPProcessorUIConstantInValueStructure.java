package com.nosliw.uiresource.page.processor;

import java.util.Map;

import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.expression1.HAPContextProcessExpressionScript;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit1;
import com.nosliw.uiresource.page.execute.HAPExecutableUITag;

public class HAPProcessorUIConstantInValueStructure {

	//resolve constants defined in context
	public static void resolveConstants(HAPExecutableUIUnit1 exeUnit, HAPRuntime runtime) {
		HAPExecutableUIUnit body = exeUnit.getBody();
		HAPContextProcessExpressionScript scriptContext = body.getProcessExpressionScriptContext();
		Map<String, Object> constantsValue = HAPUtilityStructure.discoverConstantValue(body.getValueStructureDefinitionNode().getValueStructureWrapper().getValueStructureBlock());
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
		for(HAPExecutableUITag childTag : body.getUITags()) {
			resolveConstants(childTag, runtime);			
		}
		
	}
}
