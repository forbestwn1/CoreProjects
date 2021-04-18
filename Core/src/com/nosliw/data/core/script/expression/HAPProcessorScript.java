package com.nosliw.data.core.script.expression;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.component.HAPUtilityComponentConstant;
import com.nosliw.data.core.expression.HAPContextProcessAttachmentReferenceExpression;
import com.nosliw.data.core.expression.HAPDefinitionExpressionGroupImp;
import com.nosliw.data.core.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.expression.HAPManagerExpression;
import com.nosliw.data.core.expression.HAPProcessorExpression;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.script.expression.imp.expression.HAPProcessorScriptExpression;
import com.nosliw.data.core.script.expression.imp.literate.HAPProcessorScriptLiterate;

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
		HAPDefinitionScriptGroup group = new HAPDefinitionScriptGroupImp();
		group.addEntityElement(new HAPDefinitionScriptEntity(HAPScript.newScript(script, scriptType)));
		group.setContextStructure(context);
		for(String name : constants.keySet()) {
			group.addConstantDefinition(new HAPDefinitionConstant(name, constants.get(name)));
		}
		
		HAPExecutableScriptGroup groupExe = processScript(
															null, 
															group, 
															new HAPContextProcessAttachmentReferenceExpression(null, runtimeEnv), 
															null,
															expressionMan, 
															configure, 
															runtimeEnv, 
															processTracker);
		return groupExe;
	}
	
	public static HAPExecutableScriptGroup processScript(
			String id,
			HAPDefinitionScriptGroup scriptGroupDef, 
			HAPContextProcessAttachmentReferenceExpression processContext,
			HAPContextStructure extraContext,
			HAPManagerExpression expressionMan, 
			Map<String, String> configure, 
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		HAPExecutableScriptGroup out = new HAPExecutableScriptGroup();

		//context
		HAPContextStructure contextStructure =  scriptGroupDef.getContextStructure();
		contextStructure = HAPUtilityContext.hardMerge(contextStructure, extraContext);
		out.setContextStructure(contextStructure);

		//constant
		Map<String, Object> constantsValue = HAPUtilityComponentConstant.getConstantsValue(scriptGroupDef, out.getContextFlat());
		
		//expression definition containing all expression in script 
		HAPDefinitionExpressionGroupImp expressionGroupDef = new HAPDefinitionExpressionGroupImp();
		expressionGroupDef.setContextStructure(contextStructure);

		//constant --- discover constant from attachment and context
		for(HAPDefinitionConstant def : HAPUtilityComponentConstant.getValueConstantsDefinition(scriptGroupDef, out.getContextFlat())) {
			expressionGroupDef.addConstantDefinition(def);
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
				scriptExe = HAPProcessorScriptExpression.process(scriptId, scriptDef, constantsValue, expressionGroupDef);
			}
			else if(HAPConstantShared.SCRIPT_TYPE_LITERATE.equals(type)) {
				scriptExe = HAPProcessorScriptLiterate.process(scriptId, scriptDef, constantsValue, expressionGroupDef);
			}
			else if(HAPConstantShared.SCRIPT_TYPE_TEXT.equals(type)) {
				scriptExe = new HAPExecutableScriptEntityText(scriptId, script.getScript());
			}
			out.addScript(scriptExe);
			i++;
		}
		
		HAPExecutableExpressionGroup expressionExe = HAPProcessorExpression.process(id, expressionGroupDef, processContext, null, null, expressionMan, configure, runtimeEnv, processTracker);
		out.setExpression(expressionExe);
		
		for(HAPExecutableScriptEntity script : out.getScripts()) {
			script.discoverConstantsDefinition(out.getExpression());
			script.discoverExpressionReference(out.getExpression());
			script.discoverVariablesInfo1(out.getExpression());
		}
		
		return out;
	}

}
