package com.nosliw.core.application.division.manual.brick.task.script.task;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpComplex;
import com.nosliw.core.application.entity.script.HAPWithScriptReference;
import com.nosliw.core.resource.HAPFactoryResourceId;
import com.nosliw.core.resource.HAPResourceId;

public class HAPManualPluginParserBlockTaskTaskScript extends HAPManualDefinitionPluginParserBrickImpComplex{

	public HAPManualPluginParserBlockTaskTaskScript(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan) {
		super(HAPEnumBrickType.TASK_TASK_SCRIPT_100, HAPManualDefinitionBlockTaskTaskScript.class, manualDivisionEntityMan, brickMan);
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
