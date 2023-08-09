package com.nosliw.uiresource.page.processor;

import java.util.Set;

import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.domain.entity.HAPUtilityComplexConstant;
import com.nosliw.data.core.domain.entity.expression.data.HAPDefinitionExpressionSuite1;
import com.nosliw.data.core.domain.entity.expression.data.HAPUtilityExpressionComponent;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEmbededScriptExpressionInContent;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit1;
import com.nosliw.uiresource.page.execute.HAPExecutableUITag;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInContent;

//compile definition to executable
public class HAPProcessorCompile {

	public static void process(HAPExecutableUIUnit1 exeUnit, HAPDefinitionUIUnit parentUnitDef, HAPRuntimeEnvironment runtimeEnv) {
		
		HAPDefinitionUIUnit uiUnitDef = exeUnit.getUIUnitDefinition();
		HAPExecutableUIUnit body = exeUnit.getBody();
		
		//attachment, merge with parent
		if(parentUnitDef!=null)   HAPUtilityComponent.mergeWithParentAttachment(uiUnitDef, parentUnitDef.getAttachmentContainer());

		//expression context
		body.getProcessExpressionScriptContext().setValueStructureWrapper(body.getValueStructureDefinitionNode().getValueStructureWrapper());
		
		//expression suite from attachment
		HAPDefinitionExpressionSuite1 expressionSuite = HAPUtilityExpressionComponent.buildExpressiionSuiteFromComponent(uiUnitDef, runtimeEnv);
		body.getProcessExpressionScriptContext().setExpressionDefinitionSuite(expressionSuite);
		
		//constant from attachment
		Set<HAPDefinitionConstant> constantsDef = HAPUtilityComplexConstant.buildConstantDefinition(uiUnitDef.getAttachmentContainer());
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
		for(HAPExecutableUITag childTag : body.getUITags()) {
			processAttribute(childTag, exeUnit.getUIUnitDefinition(), runtimeEnv);			
		}
		
	}
}
