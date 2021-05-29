package com.nosliw.data.core.script.expression;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.component.HAPUtilityComponentConstant;
import com.nosliw.data.core.expression.HAPContextProcessAttachmentReferenceExpression;
import com.nosliw.data.core.expression.HAPDefinitionExpressionGroupImp;
import com.nosliw.data.core.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.expression.HAPProcessorExpression;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression.imp.expression.HAPProcessorScriptExpression;
import com.nosliw.data.core.script.expression.imp.literate.HAPProcessorScriptLiterate;
import com.nosliw.data.core.structure.HAPUtilityStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinition;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;

public class HAPProcessorScript {

	public static HAPExecutableScriptGroup processSimpleScript(
			String script,
			String scriptType, 
			HAPValueStructureDefinitionFlat context, 
			Map<String, Object> constants,
			Map<String, String> configure, 
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		HAPDefinitionScriptGroup group = new HAPDefinitionScriptGroupImp();
		group.addEntityElement(new HAPDefinitionScriptEntity(HAPScript.newScript(script, scriptType)));
		group.setValueStructure(context==null?new HAPValueStructureDefinitionFlat():context);
		if(constants!=null) {
			for(String name : constants.keySet()) {
				group.addConstantDefinition(new HAPDefinitionConstant(name, constants.get(name)));
			}
		}
		
		HAPExecutableScriptGroup groupExe = processScript(
															null, 
															group, 
															new HAPContextProcessAttachmentReferenceExpression(null, runtimeEnv), 
															configure, 
															runtimeEnv, 
															processTracker);
		return groupExe;
	}
	
	public static HAPExecutableScriptGroup processScript(
			String id,
			HAPDefinitionScriptGroup scriptGroupDef, 
			HAPContextProcessAttachmentReferenceExpression processContext,
			Map<String, String> configure, 
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		HAPExecutableScriptGroup out = new HAPExecutableScriptGroup();

		//context
		HAPValueStructureDefinition valueStructure =  scriptGroupDef.getValueStructure();
		out.setValueStructureDefinition(valueStructure);

		//expression definition containing all expression in script 
		HAPDefinitionExpressionGroupImp expressionGroupDef = new HAPDefinitionExpressionGroupImp();
		//value structure for expression
		expressionGroupDef.setValueStructure(valueStructure);
		//data constant for expression
		for(HAPDefinitionConstant constantDef : HAPUtilityComponentConstant.getDataConstantsDefinition(scriptGroupDef, out.getValueStructureDefinition())) {
			expressionGroupDef.addConstantDefinition(constantDef);
		}


		//name to id updat
		HAPUpdateName name2IdUpdate = new HAPUpdateName() {
			private HAPValueStructureDefinition m_valueStructure;
			
			@Override
			public String getUpdatedName(String name) {
				HAPComplexPath namePath = new HAPComplexPath(name);
				return new HAPComplexPath(HAPUtilityStructure.resolveRoot(namePath.getRootName(), valueStructure, false).iterator().next().getLocalId(), namePath.getPath().getPath()).getFullName();
			}
		};
		
		//value constant for script
		Map<String, Object> constantsValue = HAPUtilityComponentConstant.getConstantsValue(scriptGroupDef, out.getValueStructureDefinition());

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
				scriptExe = HAPProcessorScriptExpression.process(scriptId, scriptDef, constantsValue, name2IdUpdate, expressionGroupDef);
			}
			else if(HAPConstantShared.SCRIPT_TYPE_LITERATE.equals(type)) {
				scriptExe = HAPProcessorScriptLiterate.process(scriptId, scriptDef, constantsValue, name2IdUpdate, expressionGroupDef);
			}
			else if(HAPConstantShared.SCRIPT_TYPE_TEXT.equals(type)) {
				scriptExe = new HAPExecutableScriptEntityText(scriptId, script.getScript());
			}
			out.addScript(scriptExe);
			i++;
		}
		
		HAPExecutableExpressionGroup expressionExe = HAPProcessorExpression.process(id, expressionGroupDef, processContext, null, configure, runtimeEnv, processTracker);
		out.setExpression(expressionExe);
		
		for(HAPExecutableScriptEntity script : out.getScripts()) {
			script.discoverConstantsDefinition(out.getExpression());
			script.discoverExpressionReference(out.getExpression());
			script.discoverVariables(out.getExpression());
		}
		
		return out;
	}

}
