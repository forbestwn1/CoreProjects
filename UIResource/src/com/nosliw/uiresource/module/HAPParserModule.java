package com.nosliw.uiresource.module;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.util.HAPParserProcessDefinition;
import com.nosliw.data.core.script.context.HAPParserContext;

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

		out.buildEntityInfoByJson(jsonObj);
		
		//page info
		JSONArray pageInfoArray = jsonObj.optJSONArray(HAPDefinitionModule.PAGEINFO);
		if(pageInfoArray!=null) {
			for(int i=0; i<pageInfoArray.length(); i++) {
				HAPInfoPage pageInfo = new HAPInfoPage();
				pageInfo.buildObjectByJson(pageInfoArray.get(i));
				out.addPageInfo(pageInfo);
			}
		}
		
		JSONObject contextJsonObj = jsonObj.optJSONObject(HAPDefinitionModule.CONTEXT);
		if(contextJsonObj!=null) {
			out.setContext(HAPParserContext.parseContextGroup(contextJsonObj));
		}
		
		//process
		JSONObject processJsonObject = jsonObj.optJSONObject(HAPDefinitionModule.PROCESS);
		if(processJsonObject!=null) {
			for(Object key : processJsonObject.keySet()) {
				HAPDefinitionProcess process = HAPParserProcessDefinition.parseProcess(processJsonObject.getJSONObject((String)key), m_activityPluginMan);
				process.setName((String)key);
				out.addProcess(process);
			}
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
		
		out.setPage(jsonObj.optString(HAPDefinitionModuleUI.PAGE));
		
		JSONObject contextMappingJson = jsonObj.optJSONObject(HAPDefinitionModuleUI.CONTEXTMAPPING);
		if(contextMappingJson!=null) {
			out.setContextMapping(HAPParserContext.parseContext(contextMappingJson));
		}
		
		JSONObject eventHandlersJson = jsonObj.optJSONObject(HAPDefinitionModuleUI.EVENTHANDLER);
		if(eventHandlersJson!=null) {
			for(Object key : eventHandlersJson.keySet()) {
				HAPDefinitionModuleUIEventHander eventHandler = new HAPDefinitionModuleUIEventHander();
				JSONObject eventHandlerJson = eventHandlersJson.getJSONObject((String)key);
				eventHandler.setProcess(HAPParserProcessDefinition.parseProcess(eventHandlerJson.optJSONObject(HAPDefinitionModuleUIEventHander.PROCESS), activityPluginMan));
				out.addEventHandler((String)key, eventHandler);
			}
		}
		
		return out;
	}
}
