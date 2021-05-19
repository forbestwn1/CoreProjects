package com.nosliw.data.core.valuestructure.resource;

import org.json.JSONObject;

import com.nosliw.data.core.resource.HAPParserResourceDefinitionImp;
import com.nosliw.data.core.valuestructure.HAPParserValueStructure;

public class HAPParserResourceDefinitionStructure extends HAPParserResourceDefinitionImp{

	@Override
	public HAPResourceDefinitionContext parseJson(JSONObject jsonObj) {		return new HAPResourceDefinitionContext(HAPParserValueStructure.parseValueStructureDefinitionFlat(jsonObj));	}
	
}
