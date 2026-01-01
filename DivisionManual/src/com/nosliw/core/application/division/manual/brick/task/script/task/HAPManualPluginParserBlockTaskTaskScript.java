package com.nosliw.core.application.division.manual.brick.task.script.task;

import org.json.JSONObject;

import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpComplex;

public class HAPManualPluginParserBlockTaskTaskScript extends HAPManualDefinitionPluginParserBrickImpComplex{

	public HAPManualPluginParserBlockTaskTaskScript(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan) {
		super(HAPEnumBrickType.TASK_TASK_SCRIPT_100, HAPManualDefinitionBlockTaskTaskScript.class, manualDivisionEntityMan, brickMan);
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, JSONObject jsonObj, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockTaskTaskScript taskBrick = (HAPManualDefinitionBlockTaskTaskScript)entityDefinition;

	}
}
