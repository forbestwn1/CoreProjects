package com.nosliw.uiresource.application;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializeUtility;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.util.HAPParserProcessDefinition;
import com.nosliw.data.core.script.context.HAPParserContext;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;

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
		HAPDefinitionApp out = new HAPDefinitionApp();

		out.buildEntityInfoByJson(jsonObj);
		out.setId(jsonObj.optString(HAPDefinitionApp.ID));
		out.setApplicationData(HAPSerializeUtility.buildMapFromJsonObject(HAPDefinitionAppData.class.getName(), jsonObj.optJSONObject(HAPDefinitionApp.APPLICATIONDATA)));
		out.setContext(HAPParserContext.parseContextGroup(jsonObj.optJSONObject(HAPDefinitionApp.CONTEXT))); 

		JSONArray entryArray = jsonObj.optJSONArray(HAPDefinitionApp.ENTRY);
		if(entryArray!=null) {
			for(int i=0; i<entryArray.length(); i++) {
				out.addEntry(this.parseAppEntry(entryArray.getJSONObject(i)));
			}
		}
		return out;
	}

	private HAPDefinitionAppEntryUI parseAppEntry(JSONObject jsonObj) {
		HAPDefinitionAppEntryUI out = new HAPDefinitionAppEntryUI();
		out.buildEntityInfoByJson(jsonObj);
		out.addModules(HAPSerializeUtility.buildListFromJsonArray(HAPDefinitionAppModule.class.getName(), jsonObj.optJSONArray(HAPDefinitionAppEntryUI.MODULE)));
		
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
	
}
