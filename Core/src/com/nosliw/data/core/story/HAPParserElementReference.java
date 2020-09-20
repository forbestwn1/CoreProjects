package com.nosliw.data.core.story;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPParserElementReference {

	public static HAPReferenceElement parse(JSONObject jsonObj) {
		String aliasName = (String)jsonObj.opt(HAPAliasElement.NAME);
		if(aliasName!=null) {
			HAPAliasElement out = new HAPAliasElement();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		}
		else {
			HAPIdElement out = new HAPIdElement();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		}
		return null;
	}
	
}
