package com.nosliw.uiresource.module;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.util.HAPParserProcessDefinition;
import com.nosliw.data.core.script.context.HAPParserContext;

public class HAPParserModule {

	public HAPDefinitionModule parseModuleDefinition(JSONObject jsonObj, HAPManagerActivityPlugin activityPluginMan) {
		HAPDefinitionModule out = new HAPDefinitionModule();

		out.buildEntityInfoByJson(jsonObj);
		
		//page info
		JSONArray pageInfoArray = jsonObj.optJSONArray(HAPDefinitionModule.PAGEINFO);
		if(pageInfoArray!=null) {
			for(int i=0; i<pageInfoArray.length(); i++) {
				HAPInfoPage pageInfo = new HAPInfoPage();
				pageInfo.buildObjectByJson(pageInfoArray.get(i));
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
				HAPDefinitionProcess process = HAPParserProcessDefinition.parseProcess(processJsonObject.getJSONObject((String)key), activityPluginMan);
				process.setName((String)key);
				out.addProcess(process);
			}
		}

		//ui
		JSONArray uiJsonArray = jsonObj.optJSONArray(HAPDefinitionModule.UI);
		if(uiJsonArray!=null) {
			for(int i=0; i<uiJsonArray.length(); i++) {
				HAPDefinitionModuleUI moduleUI = parseModuleUIDefinition(uiJsonArray.optJSONObject(i), activityPluginMan); 
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
