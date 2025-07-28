package com.nosliw.core.application.division.manual.brick.task.script.task;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.common.script.HAPWithScriptReference;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpComplex;
import com.nosliw.core.resource.HAPFactoryResourceId;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockTaskTaskScript extends HAPManualDefinitionPluginParserBrickImpComplex{

	public HAPManualPluginParserBlockTaskTaskScript(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.TASK_TASK_SCRIPT_100, HAPManualDefinitionBlockTaskTaskScript.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, JSONObject jsonObj, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockTaskTaskScript taskBrick = (HAPManualDefinitionBlockTaskTaskScript)entityDefinition;
		//script
		Object scriptObj = jsonObj.opt(HAPWithScriptReference.SCRIPTRESOURCEID);
		HAPResourceId scriptResourceId = HAPFactoryResourceId.tryNewInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, null, scriptObj, false);
		taskBrick.setScriptResourceId(scriptResourceId);
		
		//task interactive
		this.parseTaskInterfaceAttribute(entityDefinition, jsonObj, parseContext);
		
	}
}
