package com.nosliw.data.core.script.expression;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.data.HAPUtilityDataComponent;
import com.nosliw.data.core.expression.HAPContextProcessAttachmentReferenceExpression;
import com.nosliw.data.core.expression.HAPDefinitionExpressionGroupImp;
import com.nosliw.data.core.expression.HAPDefinitionExpressionSuite;
import com.nosliw.data.core.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.expression.HAPManagerExpression;
import com.nosliw.data.core.expression.HAPProcessorExpression;
import com.nosliw.data.core.expression.HAPUtilityExpressionComponent;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.expression.expression.HAPProcessorScriptExpression;
import com.nosliw.data.core.script.expression.literate.HAPProcessorScriptLiterate;
import com.nosliw.data.core.script.expression.resource.HAPResourceDefinitionScriptGroup;

public class HAPProcessorScript {

	public static HAPExecutableScriptGroup processSimpleScript(
			String script,
			String scriptType, 
			HAPContext context, 
			Map<String, Object> constants,
			HAPManagerExpression expressionMan,
			Map<String, String> configure, 
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		HAPContextProcessExpressionScript processScriptContext = new HAPContextProcessExpressionScript(); 
		processScriptContext.setContextStructure(context==null?new HAPContext():context);
		if(constants!=null) {
			for(String id : constants.keySet()) {
				processScriptContext.addConstantDefinition(new HAPDefinitionConstant(id, constants.get(id)));
			}
		}
		
		HAPDefinitionScriptGroup group = new HAPDefinitionScriptGroupImp();
		group.addEntityElement(new HAPDefinitionScriptEntity(HAPScript.newScript(script, scriptType)));
		
		HAPExecutableScriptGroup groupExe = processScript(null, group, processScriptContext, expressionMan, configure, runtimeEnv, processTracker);
		return groupExe;
	}
	
	public static HAPExecutableScriptGroup processScript(
			HAPResourceDefinitionScriptGroup scriptGroupDef, 
			HAPContext extraContext, 
			HAPManagerExpression expressionMan, 
			Map<String, String> configure, 
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {

		HAPContextProcessExpressionScript contextProcessScript = new HAPContextProcessExpressionScript();

		//constant
		for(HAPDefinitionConstant constantDef : HAPUtilityDataComponent.buildConstantDefinition(scriptGroupDef.getAttachmentContainer())){ 
			contextProcessScript.addConstantDefinition(constantDef);
		} 
		
		//context
		contextProcessScript.setContextStructure(scriptGroupDef.getContextStructure());
		
		//suite
		HAPDefinitionExpressionSuite expressionSuite = HAPUtilityExpressionComponent.buildExpressionSuiteFromComponent(scriptGroupDef, runtimeEnv);
		contextProcessScript.setExpressionDefinitionSuite(expressionSuite);
		
		HAPExecutableScriptGroup out = processScript(
				scriptGroupDef.getResourceId().toStringValue(HAPSerializationFormat.LITERATE),
				scriptGroupDef, 
				contextProcessScript,
				expressionMan, 
				configure, 
				runtimeEnv,
				processTracker);
		return out;
	}
	
	public static HAPExecutableScriptGroup processScript(
			String id,
			HAPDefinitionScriptGroup scriptGroupDef, 
			HAPContextProcessAttachmentReferenceExpression processContext,
			HAPManagerExpression expressionMan, 
			Map<String, String> configure, 
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		HAPExecutableScriptGroup out = new HAPExecutableScriptGroup();
		
		Map<String, Object> constantsValue = HAPUtilityDataComponent.getConstantsValue(processScriptContext);
//		out.addConstants(constantsValue);
		
		//build expression suite
		HAPDefinitionExpressionSuite expressionSuite = processScriptContext.getExpressionDefinitionSuite();

		//expression definition containing all expression in script 
		HAPDefinitionExpressionGroupImp expressionGroupDef = new HAPDefinitionExpressionGroupImp();
		expressionGroupDef.setContextStructure(processScriptContext.getContextStructure());

		for(HAPDefinitionConstant constantDef : processScriptContext.getConstantDefinitions()) {
			expressionGroupDef.addConstantDefinition(constantDef);
		}
		
		Set<HAPDefinitionScriptEntity> scriptElements = scriptGroupDef.getEntityElements();
		int i = 0;
		for(HAPDefinitionScriptEntity scriptDef : scriptElements) {
			HAPExecutableScriptEntity scriptExe = null;
			HAPScript script = scriptDef.getScript();
			//if script type not specified, discover it
			if(HAPBasicUtility.isStringEmpty(script.getType())) {
				script = HAPUtilityScriptExpression.newScript(script.getScript());
				scriptDef.setScript(script);
			}
			String type = script.getType();
			String scriptId = scriptDef.getId();
			if(HAPBasicUtility.isStringEmpty(scriptId))  scriptId = i+"";
			if(HAPConstantShared.SCRIPT_TYPE_EXPRESSION.equals(type)) {
				scriptExe = HAPProcessorScriptExpression.process(scriptId, scriptDef, scriptDef.getReference(), constantsValue, expressionGroupDef);
			}
			else if(HAPConstantShared.SCRIPT_TYPE_LITERATE.equals(type)) {
				scriptExe = HAPProcessorScriptLiterate.process(scriptId, scriptDef, scriptDef.getReference(), constantsValue, expressionGroupDef);
			}
			else if(HAPConstantShared.SCRIPT_TYPE_TEXT.equals(type)) {
				scriptExe = new HAPExecutableScriptText(scriptId, script.getScript());
			}
			out.addScript(scriptExe);
			i++;
		}
		
		HAPExecutableExpressionGroup expressionExe = HAPProcessorExpression.process(id, expressionGroupDef, processContext, null, null, expressionMan, configure, runtimeEnv, processTracker);
		out.setExpression(expressionExe);
		
		for(HAPExecutableScriptEntity script : out.getScripts()) {
			script.discoverConstantsDefinition(out.getExpression());
			script.discoverExpressionReference(out.getExpression());
			script.discoverVariablesInfo(out.getExpression());
		}
		
		return out;
	}

}
