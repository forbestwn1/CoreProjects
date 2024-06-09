package com.nosliw.data.core.script.expression1;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickWrapperValueStructure;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPUtilityComplexConstant;
import com.nosliw.data.core.domain.entity.expression.data1.HAPDefinitionEntityExpressionDataGroup;
import com.nosliw.data.core.domain.entity.expression.data1.HAPExecutableEntityExpressionDataGroup;
import com.nosliw.data.core.domain.entity.expression.data1.HAPPluginEntityDefinitionInDomainExpressionDataGroup;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression1.imp.expression.HAPProcessorScriptExpression;
import com.nosliw.data.core.script.expression1.imp.literate.HAPProcessorScriptLiterate;
import com.nosliw.data.core.valuestructure1.HAPUtilityValueStructure;

public class HAPProcessorScript {

	public static HAPExecutableScriptGroup processSimpleScript(
			String script,
			String scriptType, 
			HAPManualBrickWrapperValueStructure valueStructureWrapper, 
			Map<String, Object> constants,
			Map<String, String> configure, 
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		HAPDefinitionScriptGroup group = new HAPDefinitionScriptGroupImp();
		group.addEntityElement(new HAPDefinitionScriptEntity(HAPScript.newScript(script, scriptType)));
		group.setValueStructureWrapper(valueStructureWrapper);
		if(constants!=null) {
			for(String name : constants.keySet()) {
				group.addConstantDefinition(new HAPDefinitionConstant(name, constants.get(name)));
			}
		}
		
		HAPExecutableScriptGroup groupExe = processScript(
															null, 
															group, 
															new HAPContextProcessor(null, runtimeEnv), 
															configure, 
															runtimeEnv, 
															processTracker);
		return groupExe;
	}
	
	public static HAPExecutableScriptGroup processScript(
			String id,
			HAPDefinitionScriptGroup scriptGroupDef, 
			HAPContextProcessor processContext,
			Map<String, String> configure, 
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		HAPExecutableScriptGroup out = new HAPExecutableScriptGroup();

		//context
		HAPManualBrickWrapperValueStructure valueStructureWrapper =  scriptGroupDef.getValueStructureWrapper();
		out.setValueStructureDefinitionWrapper(valueStructureWrapper);

		//expression definition containing all expression in script 
		HAPDefinitionEntityExpressionDataGroup expressionGroupDef = new HAPDefinitionEntityExpressionDataGroup();
		//value structure for expression
		expressionGroupDef.setValueStructureWrapper(valueStructureWrapper);
		//data constant for expression
		for(HAPDefinitionConstant constantDef : HAPUtilityComplexConstant.getDataConstantsDefinition(scriptGroupDef, HAPUtilityValueStructure.getValueStructureFromWrapper(out.getValueStructureDefinitionWrapper()))) {
			expressionGroupDef.addConstantDefinition(constantDef);
		}


		//name to id updat
		HAPUpdateName name2IdUpdate = new HAPUpdateName() {
			@Override
			public String getUpdatedName(String name) {
				HAPComplexPath namePath = new HAPComplexPath(name);
				try {
					return new HAPComplexPath(HAPUtilityStructure.resolveRoot(namePath.getRoot(), valueStructureWrapper.getValueStructure(), false).iterator().next().getLocalId(), namePath.getPath().getPath()).getFullName();
				}
				catch(Throwable e) {
					e.printStackTrace();
					return null;
				}
			}
		};
		
		//value constant for script
		Map<String, Object> constantsValue = HAPUtilityComplexConstant.getConstantsValue(scriptGroupDef, HAPUtilityValueStructure.getValueStructureFromWrapper(out.getValueStructureDefinitionWrapper()));

		Set<HAPDefinitionScriptEntity> scriptElements = scriptGroupDef.getEntityElements();
		int i = 0;
		for(HAPDefinitionScriptEntity scriptDef : scriptElements) {
			HAPExecutableScriptEntity scriptExe = null;
			HAPScript script = scriptDef.getScript();
			//if script type not specified, discover it
			if(HAPUtilityBasic.isStringEmpty(script.getType())) {
				script = HAPUtilityScriptExpression1.newScript(script.getScript());
				scriptDef.setScript(script);
			}
			String type = script.getType();
			String scriptId = scriptDef.getId();
			if(HAPUtilityBasic.isStringEmpty(scriptId))  scriptId = i+"";
			if(HAPConstantShared.EXPRESSION_TYPE_SCRIPT.equals(type)) {
				scriptExe = HAPProcessorScriptExpression.process(scriptId, scriptDef, constantsValue, name2IdUpdate, expressionGroupDef);
			}
			else if(HAPConstantShared.EXPRESSION_TYPE_LITERATE.equals(type)) {
				scriptExe = HAPProcessorScriptLiterate.process(scriptId, scriptDef, constantsValue, name2IdUpdate, expressionGroupDef);
			}
			else if(HAPConstantShared.EXPRESSION_TYPE_TEXT.equals(type)) {
				scriptExe = new HAPExecutableScriptEntityText(scriptId, script.getScript());
			}
			out.addScript(scriptExe);
			scriptExe.updateVariableName(name2IdUpdate);
			i++;
		}
		
		HAPExecutableEntityExpressionDataGroup expressionExe = HAPPluginEntityDefinitionInDomainExpressionDataGroup.process(id, expressionGroupDef, processContext, null, configure, runtimeEnv, processTracker);
		out.setExpression(expressionExe);
		
		for(HAPExecutableScriptEntity script : out.getScripts()) {
			script.discoverConstantsDefinition(out.getExpression());
			script.discoverExpressionReference(out.getExpression());
			script.discoverVariables(out.getExpression());
		}
		
		return out;
	}

}
