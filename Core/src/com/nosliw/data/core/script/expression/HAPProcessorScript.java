package com.nosliw.data.core.script.expression;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPUtilityDataComponent;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.expression.HAPContextResourceExpressionGroup;
import com.nosliw.data.core.expression.HAPDefinitionExpressionGroupImp;
import com.nosliw.data.core.expression.HAPDefinitionExpressionSuite;
import com.nosliw.data.core.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.expression.HAPManagerExpression;
import com.nosliw.data.core.expression.HAPProcessorExpression;
import com.nosliw.data.core.expression.HAPUtilityExpressionComponent;
import com.nosliw.data.core.resource.HAPEntityWithResourceContext;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.expression.expression.HAPProcessorScriptExpression;
import com.nosliw.data.core.script.expression.literate.HAPProcessorScriptLiterate;
import com.nosliw.data.core.script.expression.resource.HAPResourceDefinitionScriptGroup;

public class HAPProcessorScript {

	public static HAPExecutableScriptGroup processScript(
			HAPResourceDefinitionScriptGroup scriptGroupDef, 
			HAPContext extraContext, 
			HAPManagerExpression expressionMan, 
			Map<String, String> configure, 
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {

		HAPContextProcessScript contextProcessScript = new HAPContextProcessScript();

		//constant
		for(HAPDefinitionConstant constantDef : HAPUtilityDataComponent.buildConstantDefinition(scriptGroupDef.getAttachmentContainer())){ 
			contextProcessScript.addConstantDefinition(constantDef);
		} 
		
		//context
		contextProcessScript.setContextStructure(scriptGroupDef.getContextStructure());
		
		//suite
		HAPDefinitionExpressionSuite expressionSuite = HAPUtilityExpressionComponent.buildExpressionSuiteFromComponent(scriptGroupDef);
		contextProcessScript.setExpressionDefinitionSuite(expressionSuite);
		
		HAPExecutableScriptGroup out = processScript(
				scriptGroupDef.getResourceId().toStringValue(HAPSerializationFormat.LITERATE),
				scriptGroupDef, 
				contextProcessScript,
				expressionMan, 
				configure, 
				contextProcessRequirement,
				processTracker);
		return out;
	}
	
	public static HAPExecutableScriptGroup processScript(
			String id,
			HAPDefinitionScriptGroup scriptGroupDef, 
			HAPContextProcessScript processScriptContext,
			HAPManagerExpression expressionMan, 
			Map<String, String> configure, 
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {
		HAPExecutableScriptGroup out = new HAPExecutableScriptGroup();
		
		Map<String, Object> constantsValue = HAPUtilityDataComponent.getConstantsValue(processScriptContext);
		out.addConstants(constantsValue);
		
		//build expression suite
		HAPDefinitionExpressionSuite expressionSuite = processScriptContext.getExpressionDefinitionSuite();

		//expression definition containing all expression in script 
		HAPDefinitionExpressionGroupImp expressionDef = new HAPDefinitionExpressionGroupImp();
		expressionDef.setContextStructure(processScriptContext.getContextStructure());

		for(HAPDefinitionConstant constantDef : processScriptContext.getConstantDefinitions()) {
			expressionDef.addConstantDefinition(constantDef);
		}
		
		Set<HAPDefinitionScriptEntity> scriptElements = scriptGroupDef.getEntityElements();
		int i = 0;
		for(HAPDefinitionScriptEntity scriptDef : scriptElements) {
			HAPExecutableScriptEntity scriptExe = null;
			HAPScript script = scriptDef.getScript();
			String type = script.getType();
			if(HAPConstant.SCRIPT_TYPE_EXPRESSION.equals(type)) {
				scriptExe = HAPProcessorScriptExpression.process(i+"", scriptDef, out.getConstantsValue(), expressionDef);
			}
			else if(HAPConstant.SCRIPT_TYPE_LITERATE.equals(type)) {
				scriptExe = HAPProcessorScriptLiterate.process(i+"", scriptDef, out.getConstantsValue(), expressionDef);
			}
			out.addScript(scriptExe);
			i++;
		}
		
		HAPEntityWithResourceContext resourceWithContext = new HAPEntityWithResourceContext(expressionDef, HAPContextResourceExpressionGroup.createContext(expressionSuite, contextProcessRequirement.resourceDefMan));
		HAPExecutableExpressionGroup expressionExe = HAPProcessorExpression.process(id, resourceWithContext, null, null, expressionMan, configure, contextProcessRequirement, processTracker);
		out.setExpression(expressionExe);
		
		return out;
	}

	
//	public static HAPExecutableScriptGroup processScript1(
//			HAPResourceDefinitionScriptGroup scriptGroupDef, 
//			HAPContext extraContext, 
//			HAPManagerExpression expressionMan, 
//			Map<String, String> configure, 
//			HAPRequirementContextProcessor contextProcessRequirement,
//			HAPProcessTracker processTracker) {
//		HAPExecutableScriptGroup out = new HAPExecutableScriptGroup();
//		
//		Map<String, Object> constantsValue = HAPUtilityDataComponent.buildConstantValue(scriptGroupDef.getAttachmentContainer());
//		out.addConstants(constantsValue);
//		
//		//build expression suite
//		HAPDefinitionExpressionSuite expressionSuite = HAPUtilityExpressionComponent.buildExpressionSuiteFromComponent(scriptGroupDef);
//
//		//expression definition containing all expression in script 
//		HAPDefinitionExpressionGroupImp expressionDef = new HAPDefinitionExpressionGroupImp();
//		HAPUtilityComponent.mergeWithParentAttachment(expressionDef, scriptGroupDef.getAttachmentContainer());
//		expressionDef.setContextStructure(scriptGroupDef.getContextStructure());
//		
//		List<HAPDefinitionScriptEntity> scriptElements = scriptGroupDef.getElements();
//		for(int i=0; i<scriptElements.size(); i++) {
//			HAPDefinitionScriptEntity scriptDef = scriptElements.get(i);
//			HAPExecutableScriptEntity scriptExe = null;
//			HAPScript script = scriptDef.getScript();
//			String type = script.getType();
//			if(HAPConstant.SCRIPT_TYPE_EXPRESSION.equals(type)) {
//				scriptExe = HAPProcessorScriptExpression.process(i+"", scriptDef, out.getConstantsValue(), expressionDef, expressionMan.getExpressionParser());
//			}
//			else if(HAPConstant.SCRIPT_TYPE_LITERATE.equals(type)) {
//				scriptExe = HAPProcessorScriptLiterate.process(i+"", scriptDef, out.getConstantsValue(), expressionDef, expressionMan.getExpressionParser());
//			}
//			out.addScript(scriptExe);
//		}
//		
//		HAPUtilityExpression.normalizeReference(expressionDef);
//		HAPEntityWithResourceContext resourceWithContext = new HAPEntityWithResourceContext(expressionDef, HAPContextResourceExpressionGroup.createContext(expressionSuite, contextProcessRequirement.resourceDefMan));
//		HAPExecutableExpressionGroup expressionExe = HAPProcessorExpression.process(scriptGroupDef.getResourceId().toStringValue(HAPSerializationFormat.LITERATE), resourceWithContext, extraContext, null, expressionMan, configure, contextProcessRequirement, processTracker);
//		out.setExpression(expressionExe);
//		
//		return out;
//	}
	
	
	
//	public static HAPScriptExpression processScriptExpression(HAPDefinitionScriptExpression scriptExpressionDefinition, HAPContextProcessScriptExpression expressionContext, Map<String, String> configure, HAPManagerExpression expressionManager, HAPRuntime runtime) {
//		HAPScriptExpression out = new HAPScriptExpression(scriptExpressionDefinition);
//		processScriptExpression(out, expressionContext, configure, expressionManager, runtime);
//		return out;
//	}
//	
//	public static void processScriptExpression(HAPScriptExpression scriptExpressionExe ,HAPContextProcessScriptExpression expressionContext, Map<String, String> configure, HAPManagerExpression expressionManager, HAPRuntime runtime) {
//		HAPDefinitionScriptExpression scriptExpressionDefinition = scriptExpressionExe.getDefinition();
//		for(int i=0; i<scriptExpressionDefinition.getSegments().size(); i++) {
//			Object element = scriptExpressionDefinition.getSegments().get(i);
//			if(element instanceof HAPResourceDefinitionExpression){
//				//data expression element
//				HAPResourceDefinitionExpression expEle = ((HAPResourceDefinitionExpression)element).cloneExpression();
//				
//				//preprocess attributes operand in expressions, some attributes operand can be combine into one variable operand
//				HAPUtilityScriptExpression.processAttributeOperandInExpression(expEle, expressionContext.getDataVariables());
//
//				//update with data constant
//				HAPOperandUtility.updateConstantData(expEle.getOperand(), expressionContext.getDataConstants());
//				
//				//process expression
//				HAPProcessTracker processTracker = new HAPProcessTracker();
//				HAPExecutableExpression exeExpression = expressionManager.compileExpression(expEle, expressionContext.getExpressionDefinitionSuite(), null, configure, processTracker);
//				scriptExpressionExe.addElement(exeExpression);
//			}
//			else if(element instanceof HAPScriptInScriptExpression) {
//				//script element
//				HAPScriptInScriptExpression scriptEle = ((HAPScriptInScriptExpression)element).cloneScriptInScriptExpression();
//				
//				//update with constant value
//				scriptEle.updateConstantValue(expressionContext.getConstants());
//				
//				scriptExpressionExe.addElement(scriptEle);
//			}
//		}
//
//		if(scriptExpressionExe.getVariableNames().isEmpty()){
//			//if script expression has no variable in it, then we can calculate its value
//			//execute script expression
//			HAPRuntimeTaskExecuteScriptExpression task = new HAPRuntimeTaskExecuteScriptExpression(scriptExpressionExe, null, expressionContext.getConstants());
//			HAPServiceData serviceData = runtime.executeTaskSync(task);
//			scriptExpressionExe.setValue(serviceData.getData());
//		}
//	}
//	
//	public static HAPEmbededScriptExpression processEmbededScriptExpression(HAPDefinitionEmbededScriptExpression embededScriptExpressionDefinition, HAPContextProcessScriptExpression expressionContext, Map<String, String> configure, HAPManagerExpression expressionManager, HAPRuntime runtime) {
//		HAPEmbededScriptExpression out = new HAPEmbededScriptExpression(embededScriptExpressionDefinition);
//		processEmbededScriptExpression(out, expressionContext, configure, expressionManager, runtime);		
//		return out;
//	}
//
//	public static void processEmbededScriptExpression(HAPEmbededScriptExpression embededScriptExpressionDefinitionExe, HAPContextProcessScriptExpression expressionContext, Map<String, String> configure, HAPManagerExpression expressionManager, HAPRuntime runtime) {
//		HAPDefinitionEmbededScriptExpression embededScriptExpressionDefinition = embededScriptExpressionDefinitionExe.getDefinition();
//		for(Object ele : embededScriptExpressionDefinition.getElements()) {
//			if(ele instanceof String)   embededScriptExpressionDefinitionExe.addElement(ele);
//			else if(ele instanceof HAPDefinitionScriptExpression) {
//				HAPScriptExpression scriptExpression = processScriptExpression((HAPDefinitionScriptExpression)ele, expressionContext, configure, expressionManager, runtime);
//				embededScriptExpressionDefinitionExe.addElement(scriptExpression);
//			}
//		}
//	}
}
