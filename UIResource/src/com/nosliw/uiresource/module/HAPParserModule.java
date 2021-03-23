package com.nosliw.uiresource.module;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializeUtility;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPUtilityComponentParse;
import com.nosliw.data.core.resource.HAPParserResource;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPParserDataAssociation;
import com.nosliw.uiresource.common.HAPInfoDecoration;

public class HAPParserModule implements HAPParserResource{

	public HAPParserModule() {
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
		HAPUtilityComponentParse.parseComponent(out, jsonObj);
		
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

		out.buildEntityInfoByJson(jsonObj);
		
		HAPUtilityComponentParse.parseComponentChild(out, jsonObj);
		
		out.setPage(jsonObj.optString(HAPDefinitionModuleUI.PAGE));
		out.setType(jsonObj.optString(HAPDefinitionModuleUI.TYPE));
		out.setStatus(jsonObj.optString(HAPDefinitionModuleUI.STATUS));

		//input mapping
//		out.getInputMapping().buildObject(jsonObj.optJSONArray(HAPDefinitionAppModule.INPUTMAPPING), HAPSerializationFormat.JSON);
		JSONObject inputMappingJson = jsonObj.optJSONObject(HAPDefinitionModuleUI.INPUTMAPPING);
		if(inputMappingJson!=null) {
			HAPDefinitionDataAssociation dataAssociation = HAPParserDataAssociation.buildDefinitionByJson(inputMappingJson); 
			out.addInputMapping(null, dataAssociation);
		}

		//output mapping
//		out.getOutputMapping().buildObject(jsonObj.optJSONArray(HAPDefinitionAppModule.OUTPUTMAPPING), HAPSerializationFormat.JSON);
		JSONObject outputMappingJson = jsonObj.optJSONObject(HAPDefinitionModuleUI.OUTPUTMAPPING);
		if(outputMappingJson!=null) {
			HAPDefinitionDataAssociation dataAssociation = HAPParserDataAssociation.buildDefinitionByJson(outputMappingJson);
			out.addOutputMapping(null, dataAssociation);
		}

		//ui decoration
		JSONArray uiDecJsonArray = jsonObj.optJSONArray(HAPDefinitionModuleUI.UIDECORATION);
		if(uiDecJsonArray!=null) {
			out.setUIDecoration(HAPSerializeUtility.buildListFromJsonArray(HAPInfoDecoration.class.getName(), uiDecJsonArray));
		}

		return out;
	}

}
