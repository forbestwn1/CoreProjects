package com.nosliw.miniapp.definition;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPParseMiniApp {

	public static HAPDefinitionMiniApp parseContent(String content) {
		return parseJson(new JSONObject(content));
	}
	
	private static HAPDefinitionMiniApp parseJson(JSONObject jsonObj) {
		HAPDefinitionMiniApp out = new HAPDefinitionMiniApp();
		out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return out;
	}

	public static void main(String[] args) {
		String content = HAPFileUtility.readFile("C:\\Users\\ewaniwa\\Desktop\\Mywork\\CoreProjects\\ApplicationData\\miniapp\\AppMySchool.res");
		HAPDefinitionMiniApp out = HAPParseMiniApp.parseContent(content);
		System.out.println(out);
	}
	
}
