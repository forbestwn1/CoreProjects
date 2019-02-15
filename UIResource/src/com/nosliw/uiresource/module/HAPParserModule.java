package com.nosliw.uiresource.module;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.process.HAPDefinitionEmbededProcess;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.util.HAPParserProcessDefinition;
import com.nosliw.data.core.script.context.HAPParserContext;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociationGroup;
import com.nosliw.data.core.service.use.HAPDefinitionServiceInEntity;

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
		
		//service
		JSONObject serviceJsonObject = jsonObj.optJSONObject(HAPDefinitionModule.SERVICE);
		HAPDefinitionServiceInEntity service = new HAPDefinitionServiceInEntity();
		service.buildObject(serviceJsonObject, HAPSerializationFormat.JSON);
		out.setServiceDefinition(service);
		
		//context
		JSONObject contextJsonObj = jsonObj.optJSONObject(HAPDefinitionModule.CONTEXT);
		if(contextJsonObj!=null) {
			out.setContext(HAPParserContext.parseContextGroup(contextJsonObj));
		}
		
		//process
		JSONObject processJsonObject = jsonObj.optJSONObject(HAPDefinitionModule.PROCESS);
		if(processJsonObject!=null) {
			for(Object key : processJsonObject.keySet()) {
				HAPDefinitionEmbededProcess process = HAPParserProcessDefinition.parseEmbededProcess(processJsonObject.getJSONObject((String)key), m_activityPluginMan);
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
		out.setType(jsonObj.optString(HAPDefinitionModuleUI.TYPE));
		out.setStatus(jsonObj.optString(HAPDefinitionModuleUI.STATUS));
		
		JSONObject inputMappingJson = jsonObj.optJSONObject(HAPDefinitionModuleUI.INPUTMAPPING);
		if(inputMappingJson!=null) {
			HAPDefinitionDataAssociationGroup dataAssociation = HAPDefinitionDataAssociationGroup.newWithFlatOutput();
			dataAssociation.buildObject(inputMappingJson, HAPSerializationFormat.JSON);
			out.setInputMapping(dataAssociation);
		}

		JSONObject outputMappingJson = jsonObj.optJSONObject(HAPDefinitionModuleUI.OUTPUTMAPPING);
		if(outputMappingJson!=null) {
			HAPDefinitionDataAssociationGroup dataAssociation = HAPDefinitionDataAssociationGroup.newWithoutFlatOutput();
			dataAssociation.buildObject(outputMappingJson, HAPSerializationFormat.JSON);
			out.setOutputMapping(dataAssociation);
		}

		JSONObject eventHandlersJson = jsonObj.optJSONObject(HAPDefinitionModuleUI.EVENTHANDLER);
		if(eventHandlersJson!=null) {
			for(Object key : eventHandlersJson.keySet()) {
				HAPDefinitionModuleUIEventHander eventHandler = new HAPDefinitionModuleUIEventHander();
				JSONObject eventHandlerJson = eventHandlersJson.getJSONObject((String)key);
				eventHandler.setProcess(HAPParserProcessDefinition.parseEmbededProcess(eventHandlerJson.optJSONObject(HAPDefinitionModuleUIEventHander.PROCESS), activityPluginMan));
				out.addEventHandler((String)key, eventHandler);
			}
		}
		
		return out;
	}
}
