package com.nosliw.uiresource.page.processor;

import java.util.Set;

import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.data.HAPUtilityDataComponent;
import com.nosliw.data.core.expression.HAPDefinitionExpressionSuite;
import com.nosliw.data.core.expression.HAPUtilityExpressionComponent;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEmbededScriptExpressionInContent;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIBody;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInContent;

//compile definition to executable
public class HAPProcessorCompile {

	public static void process(HAPExecutableUIUnit exeUnit, HAPDefinitionUIUnit parentUnitDef) {
		
		HAPDefinitionUIUnit uiUnitDef = exeUnit.getUIUnitDefinition();
		HAPExecutableUIBody body = exeUnit.getBody();
		
		//attachment, merge with parent
		if(parentUnitDef!=null)   HAPUtilityComponent.mergeWithParentAttachment(uiUnitDef, parentUnitDef.getAttachmentContainer());

		//expression context
		body.getProcessExpressionScriptContext().setContextStructure(body.getFlatContext().getContext());
		
		//expression suite from attachment
		HAPDefinitionExpressionSuite expressionSuite = HAPUtilityExpressionComponent.buildExpressionSuiteFromComponent(uiUnitDef, body.getFlatContext().getContext());
		body.getProcessExpressionScriptContext().setExpressionDefinitionSuite(expressionSuite);
		
		//constant from attachment
		Set<HAPDefinitionConstant> constantsDef = HAPUtilityDataComponent.buildConstantDefinition(uiUnitDef.getAttachmentContainer());
		for(HAPDefinitionConstant constantDef : constantsDef) {
			body.getProcessExpressionScriptContext().addConstantDefinition(constantDef);
		}
		
		//embeded script in content
		for(HAPDefinitionUIEmbededScriptExpressionInContent embededContent : uiUnitDef.getScriptExpressionsInContent()) {
			body.addScriptExpressionsInContent(new HAPUIEmbededScriptExpressionInContent(embededContent));
		}
		//embeded script in tag attribute 
		for(HAPDefinitionUIEmbededScriptExpressionInAttribute embededAttribute : uiUnitDef.getScriptExpressionsInAttribute()) {
			body.addScriptExpressionsInAttribute(new HAPUIEmbededScriptExpressionInAttribute(embededAttribute));
		}
		//embeded script in custom tag attribute
		for(HAPDefinitionUIEmbededScriptExpressionInAttribute embededAttribute : uiUnitDef.getScriptExpressionsInTagAttribute()) {
			body.addScriptExpressionsInTagAttribute(new HAPUIEmbededScriptExpressionInAttribute(embededAttribute));
		}
		
		//attribute
//		Map<String, String> attrs = uiUnitDef.getAttributes();
//		for(String attrName : attrs.keySet()) {
//			exeUnit.addAttribute(attrName, attrs.get(attrName));
//		}

		//child tag
		for(HAPExecutableUIUnitTag childTag : body.getUITags()) {
			process(childTag, exeUnit.getUIUnitDefinition());			
		}
		
	}
}
