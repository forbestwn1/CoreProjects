package com.nosliw.uiresource.processor;

import java.util.Map;

import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.script.expressionscript.HAPScriptExpressionUtility;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInContent;

public class HAPProcessorUIConstant {

	//resolve the name 
	public static void resolve(HAPExecutableUIUnit exeUnit) {
		Map<String, Object> constantsValue = exeUnit.getFlatContext().getConstantValue();
		
		//embeded
		for(HAPUIEmbededScriptExpressionInContent embededContent : exeUnit.getScriptExpressionsInContent())		embededContent.updateWithConstantsValue(constantsValue);
		for(HAPUIEmbededScriptExpressionInAttribute embededAttr : exeUnit.getScriptExpressionsInAttribute())		embededAttr.updateWithConstantsValue(constantsValue);
		for(HAPUIEmbededScriptExpressionInAttribute embededAttr : exeUnit.getScriptExpressionsInTagAttribute())		embededAttr.updateWithConstantsValue(constantsValue);
		
		//expression
		Map<String, HAPDefinitionExpression> expressions = exeUnit.getExpressionContext().getExpressionDefinitions();
		for(String name : expressions.keySet()) {
			HAPOperandUtility.updateConstantData(expressions.get(name).getOperand(), HAPScriptExpressionUtility.getConstantData(constantsValue));
		}
		
	}
}
