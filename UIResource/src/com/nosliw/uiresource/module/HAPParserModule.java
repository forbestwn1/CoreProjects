package com.nosliw.uiresource.module;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializeUtility;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPComponentUtility;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPParserDataAssociation;
import com.nosliw.uiresource.common.HAPInfoDecoration;

public class HAPParserModule {

	private HAPManagerActivityPlugin m_activityPluginMan;
	
	public HAPParserModule(HAPManagerActivityPlugin activityPluginMan) {
		this.m_activityPluginMan = activityPluginMan;
	}
	
	public HAPDefinitionModule parseFile(String fileName){
		HAPDefinitionModule out = null;
		try{
			File input = new File(fileName);
			//use file name as ui resource id
			String resourceId = HAPFileUtility.getFileName(input);
			String source = HAPFileUtility.readFile(input);
			out = this.parseContent(source, resourceId);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

	private HAPDefinitionModule parseContent(String content, String id) {
		JSONObject jsonObj = new JSONObject(content);
		HAPDefinitionModule out = new HAPDefinitionModule(id);

		//build component part from json object
		HAPComponentUtility.parseComponent(out, jsonObj);
		
		//ui decoration
		JSONArray uiDecJsonArray = jsonObj.optJSONArray(HAPDefinitionModule.UIDECORATION);
		if(uiDecJsonArray!=null) {
			out.setUIDecoration(HAPSerializeUtility.buildListFromJsonArray(HAPInfoDecoration.class.getName(), uiDecJsonArray));
		}
		
		//ui
		JSONArray uiJsonArray = jsonObj.optJSONArray(HAPDefinitionModule.UI);
		if(uiJsonArray!=null) {
			for(int i=0; i<uiJsonArray.length(); i++) {
				HAPDefinitionModuleUI moduleUI = parseModuleUIDefinition(uiJsonArray.optJSONObject(i), m_activityPluginMan); 
				out.addUI(moduleUI);
			}
		}
		
		return out;
	}

	public HAPDefinitionModuleUI parseModuleUIDefinition(JSONObject jsonObj, HAPManagerActivityPlugin activityPluginMan) {
		HAPDefinitionModuleUI out = new HAPDefinitionModuleUI();

		out.buildEntityInfoByJson(jsonObj);
		
		HAPComponentUtility.parseComponentChild(out, jsonObj);
		
		out.setPage(jsonObj.optString(HAPDefinitionModuleUI.PAGE));
		out.setType(jsonObj.optString(HAPDefinitionModuleUI.TYPE));
		out.setStatus(jsonObj.optString(HAPDefinitionModuleUI.STATUS));

		//input mapping
		JSONObject inputMappingJson = jsonObj.optJSONObject(HAPDefinitionModuleUI.INPUTMAPPING);
		if(inputMappingJson!=null) {
			HAPDefinitionDataAssociation dataAssociation = HAPParserDataAssociation.buildObjectByJson(inputMappingJson); 
			out.setInputMapping(dataAssociation);
		}

		//output mapping
		JSONObject outputMappingJson = jsonObj.optJSONObject(HAPDefinitionModuleUI.OUTPUTMAPPING);
		if(outputMappingJson!=null) {
			HAPDefinitionDataAssociation dataAssociation = HAPParserDataAssociation.buildObjectByJson(outputMappingJson);
			out.setOutputMapping(dataAssociation);
		}

		//ui decoration
		JSONArray uiDecJsonArray = jsonObj.optJSONArray(HAPDefinitionModuleUI.UIDECORATION);
		if(uiDecJsonArray!=null) {
			out.setUIDecoration(HAPSerializeUtility.buildListFromJsonArray(HAPInfoDecoration.class.getName(), uiDecJsonArray));
		}

		return out;
	}
}
