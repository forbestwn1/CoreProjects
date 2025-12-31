package com.nosliw.core.application.division.manual.brick.module;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.module.HAPBlockModule;
import com.nosliw.core.application.division.manual.brick.wrapperbrick.HAPManualDefinitionBrickWrapperBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpComplex;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityParserBrick;

public class HAPManualPluginParserBlockModule extends HAPManualDefinitionPluginParserBrickImpComplex{

	public HAPManualPluginParserBlockModule(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan) {
		super(HAPEnumBrickType.MODULE_100, HAPManualDefinitionBlockModule.class, manualDivisionEntityMan, brickMan);
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPManualDefinitionBrick brickDefinition, JSONObject jsonObj, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockModule moduleBrick = (HAPManualDefinitionBlockModule)brickDefinition; 
		
		JSONArray taskArrayJson = jsonObj.optJSONArray(HAPBlockModule.TASK);
		for(int i=0; i<taskArrayJson.length(); i++) {
			HAPManualDefinitionBrickWrapperBrick task = (HAPManualDefinitionBrickWrapperBrick)HAPManualDefinitionUtilityParserBrick.parseBrickDefinition(taskArrayJson.getJSONObject(i), HAPEnumBrickType.WRAPPERBRICK_100, HAPSerializationFormat.JSON, parseContext);
			moduleBrick.addTask(task);
		}
		
		JSONArray pageArrayJson = jsonObj.optJSONArray(HAPBlockModule.PAGE);
		for(int i=0; i<pageArrayJson.length(); i++) {
			HAPManualDefinitionBrickWrapperBrick page = (HAPManualDefinitionBrickWrapperBrick)HAPManualDefinitionUtilityParserBrick.parseBrickDefinition(pageArrayJson.getJSONObject(i), HAPEnumBrickType.WRAPPERBRICK_100, HAPSerializationFormat.JSON, parseContext);
			moduleBrick.addPage(page);
		}
		
	}
}
