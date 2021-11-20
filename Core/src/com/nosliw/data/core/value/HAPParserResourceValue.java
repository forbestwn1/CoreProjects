package com.nosliw.data.core.value;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPParserResourceEntity;
import com.nosliw.data.core.resource.HAPResourceDefinition1;

public class HAPParserResourceValue implements HAPParserResourceEntity{

	@Override
	public HAPResourceDefinition1 parseFile(File file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPResourceDefinition1 parseContent(String content) {
		return this.parseJson(new JSONObject(content));
	}

	@Override
	public HAPResourceDefinition1 parseJson(JSONObject jsonObj) {
		HAPResourceDefinitionValue out = new HAPResourceDefinitionValue();
		HAPValue value = new HAPValue();
		value.buildObject(jsonObj, HAPSerializationFormat.JSON);
		out.setValue(value);
		return out;
	}

}
