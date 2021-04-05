package com.nosliw.data.core.resource;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPFileUtility;

public abstract class HAPParserResourceImp implements HAPParserResource{

	@Override
	public HAPResourceDefinition parseFile(File file) {  return this.parseContent(HAPFileUtility.readFile(file)); }

	@Override
	public HAPResourceDefinition parseContent(String content) {  return this.parseJson(new JSONObject(content)); }

}
