package com.nosliw.uiresource.processor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteScriptExpression;
import com.nosliw.data.core.script.expressionscript.HAPScriptExpression;
import com.nosliw.data.core.script.expressionscript.HAPScriptExpressionUtility;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInContent;
import com.nosliw.uiresource.page.execute.HAPUtilityExecutable;

public class HAPProcessorUIConstant {

	//resolve the name 
	public static void resolveConstants(HAPExecutableUIUnit exeUnit, HAPRuntime runtime) {
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
		
		//find constant script expression, and calculate the value
		for(HAPScriptExpression scriptExpression : HAPUtilityExecutable.getScriptExpressionFromExeUnit(exeUnit)){
			if(scriptExpression.getVariableNames().isEmpty()){
				//if script expression has no variable in it, then we can calculate its value
				//execute script expression
				HAPRuntimeTaskExecuteScriptExpression task = new HAPRuntimeTaskExecuteScriptExpression(scriptExpression, null, exeUnit.getConstantsValue());
				HAPServiceData serviceData = runtime.executeTaskSync(task);
				scriptExpression.setValue(serviceData.getData());
			}
		}
	
		//when a embeded in tag attribute turn out to be constant, then replace constant value with embeded  
		Set<HAPUIEmbededScriptExpressionInAttribute> removed = new HashSet<HAPUIEmbededScriptExpressionInAttribute>();
		Set<HAPUIEmbededScriptExpressionInAttribute> all = exeUnit.getScriptExpressionsInTagAttribute();
		for(HAPUIEmbededScriptExpressionInAttribute embededScriptExpression : all){
			if(embededScriptExpression.isConstant()){
				String value = embededScriptExpression.getValue();
				HAPExecutableUIUnitTag tag = exeUnit.getUITagesByName().get(embededScriptExpression.getUIId());
				tag.addAttribute(embededScriptExpression.getAttribute(), value);
				removed.add(embededScriptExpression);
			}
		}
		for(HAPUIEmbededScriptExpressionInAttribute embededScriptExpression : removed)	all.remove(embededScriptExpression);
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : exeUnit.getUITags()) {
			resolveConstants(childTag, runtime);			
		}
		
	}
}
