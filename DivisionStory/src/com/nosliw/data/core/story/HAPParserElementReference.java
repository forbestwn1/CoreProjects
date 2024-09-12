package com.nosliw.data.core.story;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPParserElementReference {

	public static HAPReferenceElement parse(JSONObject jsonObj) {
		HAPReferenceElement out = null;
		String aliasName = (String)jsonObj.opt(HAPAliasElement.NAME);
		if(aliasName!=null) {
			out = new HAPAliasElement();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		}
		else {
			out = new HAPIdElement();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		}
		return out;
	}
	
}
