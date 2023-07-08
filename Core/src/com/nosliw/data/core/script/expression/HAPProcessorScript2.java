package com.nosliw.data.core.script.expression;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.domain.complexentity.HAPUtilityComplexConstant;
import com.nosliw.data.core.domain.entity.expression.HAPContextProcessAttachmentReferenceExpression;
import com.nosliw.data.core.domain.entity.expression.HAPDefinitionEntityExpressionGroup;
import com.nosliw.data.core.domain.entity.expression.HAPExecutableEntityExpressionGroup;
import com.nosliw.data.core.domain.entity.expression.HAPManagerExpression;
import com.nosliw.data.core.domain.entity.expression.HAPPluginEntityDefinitionInDomainExpressionGroup;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression.imp.expression.HAPProcessorScriptExpression;
import com.nosliw.data.core.script.expression.imp.literate.HAPProcessorScriptLiterate;
import com.nosliw.data.core.valuestructure1.HAPUtilityValueStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionFlat;

public class HAPProcessorScript2 {

	public static HAPExecutableScriptGroup processSimpleScript(
			String script,
			String scriptType, 
			HAPValueStructureDefinitionFlat context, 
			Map<String, Object> constants,
			HAPManagerExpression expressionMan,
			Map<String, String> configure, 
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		HAPDefinitionScriptGroup group = new HAPDefinitionScriptGroupImp();
		group.addEntityElement(new HAPDefinitionScriptEntity(HAPScript.newScript(script, scriptType)));
		group.setValueStructureWrapper(context==null?new HAPValueStructureDefinitionFlat():context);
		if(constants!=null) {
			for(String name : constants.keySet()) {
				group.addConstantDefinition(new HAPDefinitionConstant(name, constants.get(name)));
			}
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
			HAPValueStructure extraContext,
			HAPManagerExpression expressionMan, 
			Map<String, String> configure, 
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		HAPExecutableScriptGroup out = new HAPExecutableScriptGroup();

		//context
		HAPValueStructure contextStructure =  scriptGroupDef.getValueStructureWrapper();
		contextStructure = HAPUtilityValueStructure.hardMerge(contextStructure, extraContext);
		out.setContextStructure(contextStructure);

		//constant
		Map<String, Object> constantsValue = HAPUtilityComplexConstant.getConstantsValue(scriptGroupDef, out.getValueStructureFlat());
		
		//expression definition containing all expression in script 
		HAPDefinitionEntityExpressionGroup expressionGroupDef = new HAPDefinitionEntityExpressionGroup();
		expressionGroupDef.setValueContext(contextStructure);

		//constant --- discover constant from attachment and context
		for(HAPDefinitionConstant def : HAPUtilityComplexConstant.getValueConstantsDefinition(scriptGroupDef, out.getValueStructureFlat())) {
			expressionGroupDef.addConstantDefinition(def);
		}
		
		Set<HAPDefinitionScriptEntity> scriptElements = scriptGroupDef.getEntityElements();
		int i = 0;
		for(HAPDefinitionScriptEntity scriptDef : scriptElements) {
			HAPExecutableScriptEntity scriptExe = null;
			HAPScript script = scriptDef.getScript();
			//if script type not specified, discover it
			if(HAPUtilityBasic.isStringEmpty(script.getType())) {
				script = HAPUtilityScriptExpression.newScript(script.getScript());
				scriptDef.setScript(script);
			}
			String type = script.getType();
			String scriptId = scriptDef.getId();
			if(HAPUtilityBasic.isStringEmpty(scriptId))  scriptId = i+"";
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
		
		HAPExecutableEntityExpressionGroup expressionExe = HAPPluginEntityDefinitionInDomainExpressionGroup.process(id, expressionGroupDef, processContext, null, null, expressionMan, configure, runtimeEnv, processTracker);
		out.setExpression(expressionExe);
		
		for(HAPExecutableScriptEntity script : out.getScripts()) {
			script.discoverConstantsDefinition(out.getExpression());
			script.discoverExpressionReference(out.getExpression());
			script.discoverVariables(out.getExpression());
		}
		
		return out;
	}

}
