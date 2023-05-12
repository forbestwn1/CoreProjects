package com.nosliw.data.core.service.resource;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.domain.entity.service.interfacee.HAPInfoServiceInterface;
import com.nosliw.data.core.resource.HAPParserResourceEntity;
import com.nosliw.data.core.resource.HAPResourceDefinition1;

public class HAPParserServiceInterfaceResource implements HAPParserResourceEntity{

	@Override
	public HAPResourceDefinition1 parseFile(File file) {
		return this.parseContent(HAPUtilityFile.readFile(file));
	}

	@Override
	public HAPResourceDefinition1 parseContent(String content) {
		return this.parseJson(new JSONObject(content));
	}

	@Override
	public HAPResourceDefinition1 parseJson(JSONObject jsonObj) {
		HAPInfoServiceInterface out = new HAPInfoServiceInterface();
		out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return out;
	}

}
