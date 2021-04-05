package com.nosliw.data.core.script.context.resource;

import org.json.JSONObject;

import com.nosliw.data.core.resource.HAPParserResourceImp;
import com.nosliw.data.core.script.context.HAPParserContext;

public class HAPParserContextResource extends HAPParserResourceImp{

	@Override
	public HAPResourceDefinitionContext parseJson(JSONObject jsonObj) {		return new HAPResourceDefinitionContext(HAPParserContext.parseContext(jsonObj));	}
	
}
