package com.nosliw.uiresource.application;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPParseMiniApp {

	public HAPDefinitionMiniApp parseFile(String fileName){
		HAPDefinitionMiniApp out = null;
		try{
			File input = new File(fileName);
			//use file name as ui resource id
			String resourceId = HAPFileUtility.getFileName(input);
			String source = HAPFileUtility.readFile(input);
			out = this.parseContent(source);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

	
	public HAPDefinitionMiniApp parseContent(String content) {
		return parseJson(new JSONObject(content));
	}
	
	private HAPDefinitionMiniApp parseJson(JSONObject jsonObj) {
		HAPDefinitionMiniApp out = new HAPDefinitionMiniApp();
		out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return out;
	}

	public static void main(String[] args) {
		String content = HAPFileUtility.readFile("C:\\Users\\ewaniwa\\Desktop\\Mywork\\CoreProjects\\ApplicationData\\miniapp\\AppMySchool.res");
		HAPDefinitionMiniApp out = new HAPParseMiniApp().parseContent(content);
		System.out.println(out);
	}
	
}
