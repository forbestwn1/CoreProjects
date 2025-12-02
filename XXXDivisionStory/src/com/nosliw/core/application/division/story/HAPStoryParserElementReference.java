package com.nosliw.core.application.division.story;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPStoryParserElementReference {

	public static HAPStoryReferenceElement parse(JSONObject jsonObj) {
		HAPStoryReferenceElement out = null;
		String aliasName = (String)jsonObj.opt(HAPStoryAliasElement.NAME);
		if(aliasName!=null) {
			out = new HAPStoryAliasElement();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		}
		else {
			out = new HAPStoryIdElement();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		}
		return out;
	}
	
}
