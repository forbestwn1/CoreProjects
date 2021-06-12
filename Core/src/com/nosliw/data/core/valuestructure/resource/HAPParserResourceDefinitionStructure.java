package com.nosliw.data.core.valuestructure.resource;

import org.json.JSONObject;

import com.nosliw.data.core.resource.HAPParserResourceDefinitionImp;
import com.nosliw.data.core.structure.HAPParserStructure;
import com.nosliw.data.core.structure.HAPRootStructure;

public class HAPParserResourceDefinitionStructure extends HAPParserResourceDefinitionImp{

	@Override
	public HAPResourceDefinitionValueStructure parseJson(JSONObject jsonObj) {	
		HAPResourceDefinitionValueStructure out = new HAPResourceDefinitionValueStructure();
		for(Object key : jsonObj.keySet()) {
			String name = (String)key;
			JSONObject eleDefJson = jsonObj.getJSONObject(name);
			HAPRootStructure root = HAPParserStructure.parseContextRootFromJson(eleDefJson);
			root.setName(name);
			out.addRoot(root);
		}
		return out;
	}
	
}
