package com.nosliw.data.core.service.resource;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.resource.HAPParserResource;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.service.interfacee.HAPInfoServiceInterface;

public class HAPParserServiceInterfaceResource implements HAPParserResource{

	@Override
	public HAPResourceDefinition parseFile(File file) {
		return this.parseContent(HAPFileUtility.readFile(file));
	}

	@Override
	public HAPResourceDefinition parseContent(String content) {
		return this.parseJson(new JSONObject(content));
	}

	@Override
	public HAPResourceDefinition parseJson(JSONObject jsonObj) {
		HAPInfoServiceInterface out = new HAPInfoServiceInterface();
		out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return out;
	}

}
