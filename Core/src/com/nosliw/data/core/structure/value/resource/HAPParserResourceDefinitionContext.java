package com.nosliw.data.core.structure.value.resource;

import org.json.JSONObject;

import com.nosliw.data.core.resource.HAPParserResourceDefinitionImp;
import com.nosliw.data.core.structure.HAPParserContext;

public class HAPParserResourceDefinitionContext extends HAPParserResourceDefinitionImp{

	@Override
	public HAPResourceDefinitionContext parseJson(JSONObject jsonObj) {		return new HAPResourceDefinitionContext(HAPParserContext.parseValueStructureDefinitionFlat(jsonObj));	}
	
}
