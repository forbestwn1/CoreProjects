package com.nosliw.data.core.script.expression;

import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPUtilityDataComponent;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.expression.HAPContextResourceExpressionGroup;
import com.nosliw.data.core.expression.HAPDefinitionExpressionGroupImp;
import com.nosliw.data.core.expression.HAPDefinitionExpressionSuite;
import com.nosliw.data.core.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.expression.HAPManagerExpression;
import com.nosliw.data.core.expression.HAPParserExpression;
import com.nosliw.data.core.expression.HAPProcessorExpression;
import com.nosliw.data.core.expression.HAPUtilityExpression;
import com.nosliw.data.core.expression.HAPUtilityExpressionComponent;
import com.nosliw.data.core.resource.HAPEntityWithResourceContext;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.expression.expression.HAPProcessorScriptExpression;
import com.nosliw.data.core.script.expression.literate.HAPProcessorScriptLiterate;

public class HAPProcessorScript {

	public static HAPExecutableScriptGroup processScript(
			HAPResourceDefinitionScriptGroup scriptGroupDef, 
			HAPContext extraContext, 
			HAPParserExpression expressionParser, 
			HAPManagerExpression expressionMan, 
			Map<String, String> configure, 
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {
		HAPExecutableScriptGroup out = new HAPExecutableScriptGroup();
		
		Map<String, Object> constantsValue = HAPUtilityDataComponent.buildConstantValue(scriptGroupDef.getAttachmentContainer());
		out.addConstants(constantsValue);
		
		//build expression suite
		HAPDefinitionExpressionSuite expressionSuite = HAPUtilityExpressionComponent.buildExpressionSuiteFromComponent(scriptGroupDef, expressionParser);

		//expression definition containing all expression in script 
		HAPDefinitionExpressionGroupImp expressionDef = new HAPDefinitionExpressionGroupImp();
		HAPUtilityComponent.mergeWithParentAttachment(expressionDef, scriptGroupDef.getAttachmentContainer());
		expressionDef.setContextStructure(scriptGroupDef.getContextStructure());
		
		List<HAPDefinitionScriptEntity> scriptElements = scriptGroupDef.getElements();
		for(int i=0; i<scriptElements.size(); i++) {
			HAPDefinitionScriptEntity scriptDef = scriptElements.get(i);
			HAPExecutableScriptEntity scriptExe = null;
			HAPScript script = scriptDef.getScript();
			String type = script.getType();
			if(HAPConstant.SCRIPT_TYPE_EXPRESSION.equals(type)) {
				scriptExe = HAPProcessorScriptExpression.process(i+"", scriptDef, out.getConstantsValue(), expressionDef, expressionParser);
			}
			else if(HAPConstant.SCRIPT_TYPE_LITERATE.equals(type)) {
				scriptExe = HAPProcessorScriptLiterate.process(i+"", scriptDef, out.getConstantsValue(), expressionDef, expressionParser);
			}
			out.addScript(scriptExe);
		}
		
		HAPUtilityExpression.normalizeReference(expressionDef);
		HAPEntityWithResourceContext resourceWithContext = new HAPEntityWithResourceContext(expressionDef, HAPContextResourceExpressionGroup.createContext(expressionSuite, contextProcessRequirement.resourceDefMan));
		HAPExecutableExpressionGroup expressionExe = HAPProcessorExpression.process(scriptGroupDef.getResourceId().toStringValue(HAPSerializationFormat.LITERATE), resourceWithContext, extraContext, null, expressionMan, configure, contextProcessRequirement, processTracker);
		out.setExpression(expressionExe);
		
		return out;
	}
	
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
