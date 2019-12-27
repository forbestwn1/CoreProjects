package com.nosliw.uiresource.application;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPComponentUtility;
import com.nosliw.data.core.component.HAPNameMapping;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.util.HAPParserProcessDefinition;
import com.nosliw.data.core.script.context.HAPParserContext;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;
import com.nosliw.uiresource.common.HAPUtilityParser;
import com.nosliw.uiresource.module.HAPDefinitionModuleUI;

public class HAPParseMiniApp {

	private HAPManagerActivityPlugin m_activityPluginMan;
	
	public HAPParseMiniApp(HAPManagerActivityPlugin activityPluginMan) {
		this.m_activityPluginMan = activityPluginMan;
	}
	
	public HAPDefinitionApp parseFile(String fileName){
		HAPDefinitionApp out = null;
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

	
	public HAPDefinitionApp parseContent(String content) {
		return parseAppJson(new JSONObject(content));
	}
	
	private HAPDefinitionApp parseAppJson(JSONObject jsonObj) {
		HAPDefinitionApp out = new HAPDefinitionApp(jsonObj.optString(HAPDefinitionApp.ID));

		HAPComponentUtility.parseComponent(out, jsonObj);
		
		out.setApplicationData(HAPSerializeUtility.buildMapFromJsonObject(HAPDefinitionAppData.class.getName(), jsonObj.optJSONObject(HAPDefinitionApp.APPLICATIONDATA)));

		JSONArray entryArray = jsonObj.optJSONArray(HAPDefinitionApp.ENTRY);
		if(entryArray!=null) {
			for(int i=0; i<entryArray.length(); i++) {
				out.addEntry(this.parseAppEntry(entryArray.getJSONObject(i)));
			}
		}
		return out;
	}

	private HAPDefinitionAppEntryUI parseAppEntry(JSONObject jsonObj) {
		HAPDefinitionAppEntryUI out = new HAPDefinitionAppEntryUI(null);

		HAPComponentUtility.parseComponent(out, jsonObj);

		JSONArray moduleArrayJson = jsonObj.optJSONArray(HAPDefinitionAppEntryUI.MODULE);
		for(int i=0; i<moduleArrayJson.length(); i++) {
			out.addModule(parseModule(moduleArrayJson.getJSONObject(i), m_activityPluginMan));
		}
		
		JSONObject processesJson = jsonObj.optJSONObject(HAPDefinitionAppEntryUI.PROCESS);
		if(processesJson!=null) {
			for(Object key :processesJson.keySet()) {
				String processName = (String)key;
				JSONObject processJson = processesJson.getJSONObject(processName);

				HAPDefinitionWrapperTask<HAPDefinitionProcess> process = HAPParserProcessDefinition.parseEmbededProcess(processJson, this.m_activityPluginMan);
				process.getTaskDefinition().setName((String)key);
				out.addProcess(processName, process);
			}
		}
		
		out.setContext(HAPParserContext.parseContextGroup(jsonObj.optJSONObject(HAPDefinitionAppEntryUI.CONTEXT))); 
		return out;
	}
	
	private HAPDefinitionAppModule parseModule(JSONObject moduleJson, HAPManagerActivityPlugin activityPluginMan) {
		HAPDefinitionAppModule out = new HAPDefinitionAppModule();
		out.buildEntityInfoByJson(moduleJson);
		out.setRole((String)moduleJson.opt(HAPDefinitionAppModule.ROLE));
		out.setStatus((String)moduleJson.opt(HAPDefinitionModuleUI.STATUS));
		out.getInputMapping().buildObject(moduleJson.optJSONArray(HAPDefinitionAppModule.INPUTMAPPING), HAPSerializationFormat.JSON);
		out.getOutputMapping().buildObject(moduleJson.optJSONArray(HAPDefinitionAppModule.OUTPUTMAPPING), HAPSerializationFormat.JSON);
		
		out.setNameMapping(HAPNameMapping.newNamingMapping(moduleJson.optJSONObject(HAPDefinitionModuleUI.NAMEMAPPING)));
		
		JSONObject eventHandlersJson = moduleJson.optJSONObject(HAPDefinitionAppModule.EVENTHANDLER);
		out.addEventHandler(HAPUtilityParser.parseEventHandlers(eventHandlersJson, activityPluginMan));

		return out;
	}
}
