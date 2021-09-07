package com.nosliw.uiresource.module;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPParserComponent;
import com.nosliw.data.core.resource.HAPParserResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.uiresource.common.HAPInfoDecoration;

public class HAPParserModule implements HAPParserResourceDefinition{

	HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPParserModule(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
	}
	
	public HAPDefinitionModule parseFile(String fileName){		return parseFile(new File(fileName));	}
	
	@Override
	public HAPDefinitionModule parseFile(File file){
		HAPDefinitionModule out = null;
		try{
			//use file name as ui resource id
			String resourceId = HAPFileUtility.getFileName(file);
			String source = HAPFileUtility.readFile(file);
			out = this.parseContent(source, resourceId);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

	@Override
	public HAPResourceDefinition parseContent(String content) {  return this.parseContent(content, null);	}

	private HAPDefinitionModule parseContent(String content, String id) {
		JSONObject jsonObj = new JSONObject(content);
		HAPDefinitionModule out = parseJson(jsonObj);
		out.setId(id);
		return out;
	}

	@Override
	public HAPDefinitionModule parseJson(JSONObject jsonObj) {
		HAPDefinitionModule out = new HAPDefinitionModule();

		//build component part from json object
		HAPParserComponent.parseComponent(out, jsonObj, this.m_runtimeEnv.getTaskManager());
		
		//ui decoration
		JSONArray uiDecJsonArray = jsonObj.optJSONArray(HAPDefinitionModule.UIDECORATION);
		if(uiDecJsonArray!=null) {
			out.setUIDecoration(HAPSerializeUtility.buildListFromJsonArray(HAPInfoDecoration.class.getName(), uiDecJsonArray));
		}
		
		//ui
		JSONArray uiJsonArray = jsonObj.optJSONArray(HAPDefinitionModule.UI);
		if(uiJsonArray!=null) {
			for(int i=0; i<uiJsonArray.length(); i++) {
				HAPDefinitionModuleUI moduleUI = parseModuleUIDefinition(uiJsonArray.optJSONObject(i)); 
				out.addUI(moduleUI);
			}
		}
		
		return out;
	}
	
	public HAPDefinitionModuleUI parseModuleUIDefinition(JSONObject jsonObj) {
		HAPDefinitionModuleUI out = new HAPDefinitionModuleUI();
		out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return out;
	}
}
