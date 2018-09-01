package com.nosliw.uiresource.processor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.script.expressionscript.HAPScriptExpressionUtility;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitTag;
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


	//when a embeded in tag attribute turn out to be constant, then replace constant value with embeded  
	private static void processConstantExpressionInAttributeTag(HAPDefinitionUIUnit uiDefinitionUnit){
		Set<HAPUIEmbededScriptExpressionInAttribute> removed = new HashSet<HAPUIEmbededScriptExpressionInAttribute>();
		Set<HAPUIEmbededScriptExpressionInAttribute> all = uiDefinitionUnit.getScriptExpressionsInTagAttribute();
		for(HAPUIEmbededScriptExpressionInAttribute embededScriptExpression : all){
			if(embededScriptExpression.isConstant()){
				String value = embededScriptExpression.getValue();
				HAPDefinitionUIUnitTag tag = uiDefinitionUnit.getUITagesByName().get(embededScriptExpression.getUIId());
				tag.addAttribute(embededScriptExpression.getAttribute(), value);
				removed.add(embededScriptExpression);
			}
		}
		
		for(HAPUIEmbededScriptExpressionInAttribute embededScriptExpression : removed)	all.remove(embededScriptExpression);
		
		for(HAPDefinitionUIUnit childTag : uiDefinitionUnit.getUITags())		processConstantExpressionInAttributeTag(childTag);
	}
}
