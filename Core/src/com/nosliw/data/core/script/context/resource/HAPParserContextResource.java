package com.nosliw.data.core.script.context.resource;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.resource.HAPParserResource;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.script.context.HAPParserContext;

public class HAPParserContextResource implements HAPParserResource{

	@Override
	public HAPResourceDefinition parseFile(File file) {  return this.parseContent(HAPFileUtility.readFile(file)); }

	@Override
	public HAPResourceDefinition parseContent(String content) {  return this.parseJson(new JSONObject(content)); }

	@Override
	public HAPResourceDefinitionContext parseJson(JSONObject jsonObj) {		return new HAPResourceDefinitionContext(HAPParserContext.parseContext(jsonObj));	}
	
}
