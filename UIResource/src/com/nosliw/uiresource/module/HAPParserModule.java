package com.nosliw.uiresource.module;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.util.HAPParserProcessDefinition;
import com.nosliw.data.core.script.context.HAPParserContext;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;
import com.nosliw.data.core.script.context.dataassociation.HAPParserDataAssociation;
import com.nosliw.data.core.service.use.HAPDefinitionServiceInEntity;
import com.nosliw.uiresource.common.HAPUtilityParser;

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
				HAPDefinitionWrapperTask<HAPDefinitionProcess> process = HAPParserProcessDefinition.parseEmbededProcess(processJsonObject.getJSONObject((String)key), m_activityPluginMan);
				process.getTaskDefinition().setName((String)key);
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
			HAPDefinitionDataAssociation dataAssociation = HAPParserDataAssociation.buildObjectByJson(inputMappingJson); 
			out.setInputMapping(dataAssociation);
		}

		JSONObject outputMappingJson = jsonObj.optJSONObject(HAPDefinitionModuleUI.OUTPUTMAPPING);
		if(outputMappingJson!=null) {
			HAPDefinitionDataAssociation dataAssociation = HAPParserDataAssociation.buildObjectByJson(outputMappingJson);
			out.setOutputMapping(dataAssociation);
		}

		JSONObject eventHandlersJson = jsonObj.optJSONObject(HAPDefinitionModuleUI.EVENTHANDLER);
		out.addEventHandler(HAPUtilityParser.parseEventHandlers(eventHandlersJson, activityPluginMan));
		return out;
	}
	
}
