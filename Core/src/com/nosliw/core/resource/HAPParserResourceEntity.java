package com.nosliw.core.resource;

import java.io.File;

import org.json.JSONObject;

public interface HAPParserResourceEntity {

	HAPEntityResourceDefinition parseFile(File file);
	
	HAPEntityResourceDefinition parseContent(String content);

	HAPEntityResourceDefinition parseJson(JSONObject jsonObj);

}
