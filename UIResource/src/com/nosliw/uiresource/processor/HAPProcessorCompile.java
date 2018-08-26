package com.nosliw.uiresource.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.script.expressionscript.HAPDefinitionEmbededScript;
import com.nosliw.data.core.script.expressionscript.HAPScriptExpression;
import com.nosliw.data.core.script.expressionscript.HAPScriptExpressionUtility;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEmbededScriptExpressionInContent;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInContent;

//compile expression definition to expression
public class HAPProcessorCompile {

	public static void compile(HAPExecutableUIUnit exeUnit) {
		
		HAPDefinitionUIUnit uiUnitDef = exeUnit.getUIUnitDefinition();
		
		//embeded script in context
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
		
	}
	
	
	
	
}
