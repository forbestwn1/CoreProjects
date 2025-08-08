package com.nosliw.uiresource.application;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilitySerialize;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.xxx.application.common.structure.HAPParserContext;
import com.nosliw.data.core.component.HAPParserEntityComponent;
import com.nosliw.data.core.domain.complexentity.HAPDefinitionEntityContainer;
import com.nosliw.uiresource.module.HAPDefinitionModuleUI;

public class HAPParseMiniApp {

	public HAPParseMiniApp() {
	}
	
	public HAPDefinitionApp parseFile(String fileName){
		HAPDefinitionApp out = null;
		try{
			File input = new File(fileName);
			//use file name as ui resource id
			String resourceId = HAPUtilityFile.getFileName(input);
			String source = HAPUtilityFile.readFile(input);
			out = this.parseMiniApp(source);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

	public HAPDefinitionApp parseMiniApp(String content) {
		return parseMiniApp(new JSONObject(content));
	}
	
	public HAPDefinitionApp parseMiniApp(JSONObject jsonObj) {
		HAPDefinitionApp out = new HAPDefinitionApp(jsonObj.optString(HAPDefinitionApp.ID));

		HAPParserEntityComponent.parseComplextResourceDefinition(out, jsonObj);
		
		out.setApplicationData(HAPUtilitySerialize.buildMapFromJsonObject(HAPDefinitionAppData.class.getName(), jsonObj.optJSONObject(HAPDefinitionApp.APPLICATIONDATA)));

		JSONArray entryArray = jsonObj.optJSONArray(HAPDefinitionEntityContainer.ELEMENT);
		if(entryArray!=null) {
			for(int i=0; i<entryArray.length(); i++) {
				out.addEntry(this.parseAppEntry(entryArray.getJSONObject(i)));
			}
		}
		return out;
	}

	private HAPDefinitionAppElementUI parseAppEntry(JSONObject jsonObj) {
		HAPDefinitionAppElementUI out = new HAPDefinitionAppElementUI(null);

		HAPParserEntityComponent.parseComponent(out, jsonObj);

		JSONArray moduleArrayJson = jsonObj.optJSONArray(HAPDefinitionAppElementUI.MODULE);
		for(int i=0; i<moduleArrayJson.length(); i++) {
			out.addModule(parseModule(moduleArrayJson.getJSONObject(i)));
		}
		
//		JSONObject processesJson = jsonObj.optJSONObject(HAPDefinitionAppEntryUI.PROCESS);
//		if(processesJson!=null) {
//			for(Object key :processesJson.keySet()) {
//				String processName = (String)key;
//				JSONObject processJson = processesJson.getJSONObject(processName);
//
//				HAPDefinitionWrapperTask<HAPDefinitionProcessSuiteElementEntity> process = HAPParserProcessDefinition.parseEmbededProcess(processJson, this.m_activityPluginMan);
//				process.getTaskDefinition().setName((String)key);
//				out.addProcess(processName, process);
//			}
//		}
		
		out.setValueStructureDefinitionWrapper(HAPParserContext.parseContextGroup(jsonObj.optJSONObject(HAPDefinitionAppElementUI.VALUESTRUCTURE))); 
		return out;
	}
	
	private HAPDefinitionAppModule parseModule(JSONObject moduleJson) {
		HAPDefinitionAppModule out = new HAPDefinitionAppModule();
		out.buildEntityInfoByJson(moduleJson);
		out.setModule((String)moduleJson.opt(HAPDefinitionAppModule.MODULE));
		out.setRole((String)moduleJson.opt(HAPDefinitionAppModule.ROLE));
		out.setStatus((String)moduleJson.opt(HAPDefinitionModuleUI.STATUS));
		out.getInDataAssociations().buildObject(moduleJson.optJSONArray(HAPDefinitionAppModule.IN), HAPSerializationFormat.JSON);
		out.getOutDataAssociations().buildObject(moduleJson.optJSONArray(HAPDefinitionAppModule.OUT), HAPSerializationFormat.JSON);
		
		HAPParserEntityComponent.parseComponentChild(out, moduleJson);
		return out;
	}
}
