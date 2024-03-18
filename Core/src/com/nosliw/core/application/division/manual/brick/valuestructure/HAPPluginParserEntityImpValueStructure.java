package com.nosliw.core.application.division.manual.brick.valuestructure;

import java.util.List;

import org.json.JSONObject;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualContextParse;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPPluginParserBrickImpSimple;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginParserEntityImpValueStructure extends HAPPluginParserBrickImpSimple{

	public HAPPluginParserEntityImpValueStructure(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.VALUESTRUCTURE_100, HAPDefinitionEntityValueStructure.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentJson(HAPManualBrick entityDefinition, Object jsonValue, HAPManualContextParse parseContext) {
		JSONObject structureJson = (JSONObject)jsonValue;
		if(structureJson!=null) {
			HAPDefinitionEntityValueStructure valueStructure = (HAPDefinitionEntityValueStructure)entityDefinition;
			Object elementsObj = structureJson.opt(HAPDefinitionEntityValueStructure.VALUE);
			if(elementsObj==null) {
				elementsObj = structureJson;
			}
			List<HAPRootStructure> roots = HAPParserValueStructure.parseStructureRoots(elementsObj);
			for(HAPRootStructure root : roots) {
				valueStructure.addRoot(root);
			}
		}
	}
}
