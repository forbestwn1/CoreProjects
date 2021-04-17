package com.nosliw.data.core.value;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPParserResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;

public class HAPParserResourceValue implements HAPParserResourceDefinition{

	@Override
	public HAPResourceDefinition parseFile(File file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPResourceDefinition parseContent(String content) {
		return this.parseJson(new JSONObject(content));
	}

	@Override
	public HAPResourceDefinition parseJson(JSONObject jsonObj) {
		HAPResourceDefinitionValue out = new HAPResourceDefinitionValue();
		HAPValue value = new HAPValue();
		value.buildObject(jsonObj, HAPSerializationFormat.JSON);
		out.setValue(value);
		return out;
	}

}
