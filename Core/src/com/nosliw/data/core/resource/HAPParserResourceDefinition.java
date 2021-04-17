package com.nosliw.data.core.resource;

import java.io.File;

import org.json.JSONObject;

public interface HAPParserResourceDefinition {

	HAPResourceDefinition parseFile(File file);
	
	HAPResourceDefinition parseContent(String content);

	HAPResourceDefinition parseJson(JSONObject jsonObj);
	

}
