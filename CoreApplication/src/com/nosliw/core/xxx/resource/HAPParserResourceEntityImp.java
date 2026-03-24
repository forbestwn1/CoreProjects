package com.nosliw.core.xxx.resource;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPUtilityFile;

public abstract class HAPParserResourceEntityImp implements HAPParserResourceEntity{

	@Override
	public HAPEntityResourceDefinition parseFile(File file) {  return this.parseContent(HAPUtilityFile.readFile(file)); }

	@Override
	public HAPEntityResourceDefinition parseContent(String content) {  return this.parseJson(new JSONObject(content)); }

}
