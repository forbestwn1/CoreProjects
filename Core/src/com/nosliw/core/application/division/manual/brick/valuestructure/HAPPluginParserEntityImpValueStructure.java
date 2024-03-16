package com.nosliw.core.application.division.manual.brick.valuestructure;

import java.util.List;

import org.json.JSONObject;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPContextParse;
import com.nosliw.core.application.division.manual.HAPManagerEntityDivisionManual;
import com.nosliw.core.application.division.manual.HAPManualEntity;
import com.nosliw.core.application.division.manual.HAPPluginParserEntityImpSimple;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginParserEntityImpValueStructure extends HAPPluginParserEntityImpSimple{

	public HAPPluginParserEntityImpValueStructure(HAPManagerEntityDivisionManual manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.VALUESTRUCTURE_100, HAPDefinitionEntityValueStructure.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentJson(HAPManualEntity entityDefinition, Object jsonValue, HAPContextParse parseContext) {
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
