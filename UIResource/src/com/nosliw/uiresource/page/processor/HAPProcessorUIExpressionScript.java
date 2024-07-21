package com.nosliw.uiresource.page.processor;

import java.util.Set;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPUtilityComplexConstant;
import com.nosliw.data.core.domain.entity.expression.data1.HAPDefinitionExpressionSuite1;
import com.nosliw.data.core.domain.entity.expression.data1.HAPUtilityExpressionComponent;
import com.nosliw.data.core.domain.entity.expression.data1.HAPUtilityExpressionProcessConfigure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression1.HAPContextProcessExpressionScript;
import com.nosliw.data.core.script.expression1.HAPDefinitionScriptGroupImp;
import com.nosliw.data.core.script.expression1.HAPExecutableScriptGroup;
import com.nosliw.data.core.script.expression1.HAPProcessorScript;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEmbededScriptExpressionInContent;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit1;
import com.nosliw.uiresource.page.execute.HAPExecutableUITag;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInContent;

public class HAPProcessorUIExpressionScript {

	//constants
//	Map<String, Object> constantsValue = flatContext.getConstantValue();
//	for(String name : constantsValue.keySet()) {
//		Object constantValue = constantsValue.get(name);
//		uiExe.addConstantValue(name, constantValue);
//	}
	
	//build variables
//	uiExe.getExpressionSuiteInContext().setContextStructure(flatContext.getContext());
//	Map<String, HAPVariableDefinition> varsInfo = HAPUtilityContext.discoverDataVariablesInContext(flatContext.getContext());
//	for(String varName : varsInfo.keySet()) {
//		uiExe.getExpressionContext().addDataVariable(varName, varsInfo.get(varName));
//	}
	
	public static void buildExpressionScriptProcessContext(HAPExecutableUIUnit1 exeUnit, HAPRuntimeEnvironment runtimeEnv){
		HAPExecutableUIUnit body = exeUnit.getBody();
		HAPDefinitionUIUnit uiUnitDef = exeUnit.getUIUnitDefinition();

		HAPContextProcessExpressionScript processScriptContext = body.getProcessExpressionScriptContext();
		
		//expression context
		processScriptContext.setValueStructureWrapper(body.getValueStructureDefinitionNode().getValueStructureWrapper());
		
		//expression suite from attachment
		HAPDefinitionExpressionSuite1 expressionSuite = HAPUtilityExpressionComponent.buildExpressiionSuiteFromComponent(uiUnitDef, runtimeEnv);
		processScriptContext.setExpressionDefinitionSuite(expressionSuite);
		
		//constant from attachment and context
		Set<HAPDefinitionConstant> constantsDef = HAPUtilityComplexConstant.buildConstantDefinition(uiUnitDef.getAttachmentContainer(), body.getValueStructureDefinitionNode().getValueStructureWrapper().getValueStructureBlock());
		for(HAPDefinitionConstant constantDef : constantsDef) {
			processScriptContext.addConstantDefinition(constantDef);
		}
		
		//child tag
		for(HAPExecutableUITag childTag : body.getUITags()) {
			buildExpressionScriptProcessContext(childTag, runtimeEnv);			
		}
	}
	
	public static void processUIScriptExpression(HAPExecutableUIUnit1 exeUnit, HAPRuntimeEnvironment runtimeEnv){
		HAPExecutableUIUnit body = exeUnit.getBody();
		HAPDefinitionUIUnit uiUnitDef = exeUnit.getUIUnitDefinition();
		
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

		
		HAPDefinitionScriptGroupImp scriptGroup = new HAPDefinitionScriptGroupImp();
		scriptGroup.setValueStructureWrapper(exeUnit.getBody().getValueStructureDefinitionNode().getValueStructureWrapper());
		for(HAPDefinitionConstant constantDef : HAPUtilityComplexConstant.buildConstantDefinition(uiUnitDef.getAttachmentContainer(), exeUnit.getBody().getValueStructureDefinitionNode().getValueStructureWrapper().getValueStructureBlock())) {
			scriptGroup.addConstantDefinition(constantDef);
		}
		
		for(HAPDefinitionUIEmbededScriptExpressionInContent embededContent : uiUnitDef.getScriptExpressionsInContent()) {
			scriptGroup.addEntityElement(embededContent);
		}
		//embeded script in tag attribute 
		for(HAPDefinitionUIEmbededScriptExpressionInAttribute embededAttribute : uiUnitDef.getScriptExpressionsInAttribute()) {
			scriptGroup.addEntityElement(embededAttribute);
		}
		//embeded script in custom tag attribute
		for(HAPDefinitionUIEmbededScriptExpressionInAttribute embededAttribute : uiUnitDef.getScriptExpressionsInTagAttribute()) {
			scriptGroup.addEntityElement(embededAttribute);
		}

		HAPExecutableScriptGroup scriptGroupExe = HAPProcessorScript.processScript(
				exeUnit.getId(), 
				scriptGroup, 
				new HAPContextProcessor(uiUnitDef, runtimeEnv), 
				HAPUtilityExpressionProcessConfigure.setDoDiscovery(null), 
				runtimeEnv, 
				new HAPProcessTracker()
		);
		body.setScriptGroupExecutable(scriptGroupExe);
		
//		//process all embeded script expression
//		List<HAPEmbededScriptExpression> embededScriptExpressions = HAPUtilityExecutable.getEmbededScriptExpressionFromExeUnit(exeUnit);
//		for(HAPEmbededScriptExpression embededScriptExpression : embededScriptExpressions) {
//			HAPProcessorScript.processEmbededScriptExpression(embededScriptExpression, exeUnit.getExpressionContext(), HAPUtilityExpressionProcessConfigure.setDoDiscovery(null), expressionManager, runtime);
//		}
//
//		//when a embeded in tag attribute turn out to be constant, then replace constant value with embeded  
//		Set<HAPUIEmbededScriptExpressionInAttribute> removed = new HashSet<HAPUIEmbededScriptExpressionInAttribute>();
//		Set<HAPUIEmbededScriptExpressionInAttribute> all = exeUnit.getScriptExpressionsInTagAttribute();
//		for(HAPUIEmbededScriptExpressionInAttribute embededScriptExpression : all){
//			if(embededScriptExpression.isConstant()){
//				String value = embededScriptExpression.getValue();
//				HAPExecutableUIUnitTag tag = exeUnit.getUITagesByName().get(embededScriptExpression.getUIId());
//				tag.addAttribute(embededScriptExpression.getAttribute(), value);
//				removed.add(embededScriptExpression);
//			}
//		}
//		for(HAPUIEmbededScriptExpressionInAttribute embededScriptExpression : removed)	all.remove(embededScriptExpression);

		
		//child tag
		for(HAPExecutableUITag childTag : body.getUITags()) {
			processUIScriptExpression(childTag, runtimeEnv);			
		}
	}
	
}
