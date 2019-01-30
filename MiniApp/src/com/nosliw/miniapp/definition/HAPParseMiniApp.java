package com.nosliw.miniapp.definition;

import org.json.JSONObject;

public class HAPParseMiniApp {

	
	public HAPDefinitionMiniApp parseContent(String content) {
		return this.parseJson(new JSONObject(content));
	}
	

	private HAPDefinitionMiniApp parseJson(JSONObject jsonObj) {
		HAPDefinitionMiniApp out = new HAPDefinitionMiniApp();
		out.buildEntityInfoByJson(jsonObj);
		return out;
	}

}
