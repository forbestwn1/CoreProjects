package com.nosliw.uiresource.processor;

import java.util.Map;

import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.script.expressionscript.HAPScriptExpressionUtility;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEmbededScriptExpressionInContent;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInContent;

//compile definition to executable
public class HAPProcessorCompile {

	public static void process(HAPExecutableUIUnit exeUnit, HAPExecutableUIUnit parentUnit) {
		
		HAPDefinitionUIUnit uiUnitDef = exeUnit.getUIUnitDefinition();
		
		//embeded script in content
		for(HAPDefinitionUIEmbededScriptExpressionInContent embededContent : uiUnitDef.getScriptExpressionsInContent()) {
			exeUnit.addScriptExpressionsInContent(new HAPUIEmbededScriptExpressionInContent(embededContent.getUIId(), HAPScriptExpressionUtility.toExeEmbedElement(embededContent.getElements())));
		}
		//embeded script in tag attribute 
		for(HAPDefinitionUIEmbededScriptExpressionInAttribute embededAttribute : uiUnitDef.getScriptExpressionsInAttribute()) {
			exeUnit.addScriptExpressionsInAttribute(new HAPUIEmbededScriptExpressionInAttribute(embededAttribute.getAttribute(), embededAttribute.getUIId(), HAPScriptExpressionUtility.toExeEmbedElement(embededAttribute.getElements())));
		}
		//embeded script in custom tag attribute
		for(HAPDefinitionUIEmbededScriptExpressionInAttribute embededAttribute : uiUnitDef.getScriptExpressionsInTagAttribute()) {
			exeUnit.addScriptExpressionsInTagAttribute(new HAPUIEmbededScriptExpressionInAttribute(embededAttribute.getAttribute(), embededAttribute.getUIId(), HAPScriptExpressionUtility.toExeEmbedElement(embededAttribute.getElements())));
		}
		
		//attribute
		Map<String, String> attrs = uiUnitDef.getAttributes();
		for(String attrName : attrs.keySet()) {
			exeUnit.addAttribute(attrName, attrs.get(attrName));
		}

		//other expressions : merge with parent + convert
		if(parentUnit!=null) {
			//from parent
			Map<String, String> parentExps = parentUnit.getUIUnitDefinition().getExpressionDefinitions();
			for(String name : parentExps.keySet()) 		exeUnit.getExpressionContext().addExpressionDefinition(name, new HAPDefinitionExpression(parentExps.get(name)));
			
			//it self
			Map<String, String> expressions = uiUnitDef.getExpressionDefinitions();
			for(String name : expressions.keySet()) 		exeUnit.getExpressionContext().addExpressionDefinition(name, new HAPDefinitionExpression(expressions.get(name)));
		}
	
		//child tag
		for(HAPExecutableUIUnitTag childTag : exeUnit.getUITags()) {
			process(childTag, exeUnit);			
		}
		
	}
}
