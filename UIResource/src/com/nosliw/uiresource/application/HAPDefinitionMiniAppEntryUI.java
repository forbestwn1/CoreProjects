package com.nosliw.uiresource.application;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;
import com.nosliw.data.core.process.HAPDefinitionEmbededProcess;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.uiresource.common.HAPComponentWithConfiguration;

@HAPEntityWithAttribute
public class HAPDefinitionMiniAppEntryUI  extends HAPComponentWithConfiguration implements HAPDefinitionMiniAppEntry{

	@HAPAttribute
	public static final String MODULE = "module";

	@HAPAttribute
	public static final String PROCESS = "process";

	@HAPAttribute
	public static final String CONTEXT = "context";

	//all modules in this entry
	private List<HAPDefinitionMiniAppModule> m_modules;

	private Map<String, HAPDefinitionEmbededProcess> m_processes;

	//context data shared by different module
	private HAPContextGroup m_context;
	
	public HAPDefinitionMiniAppEntryUI() {
		this.m_modules = new ArrayList<HAPDefinitionMiniAppModule>();
		this.m_processes = new LinkedHashMap<String, HAPDefinitionEmbededProcess>();
	}
	
	public List<HAPDefinitionMiniAppModule> getModules(){  return this.m_modules;  }
	public HAPDefinitionEmbededProcess getProcess(String name) {  return this.m_processes.get(name);   }
	public Map<String, HAPDefinitionEmbededProcess> getProcesses(){   return this.m_processes;  }
	public HAPContextGroup getContext() {  return this.m_context;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(MODULE, HAPJsonUtility.buildJson(this.m_modules, HAPSerializationFormat.JSON));
		jsonMap.put(PROCESS, HAPJsonUtility.buildJson(this.m_processes, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_modules =  HAPSerializeUtility.buildListFromJsonArray(HAPDefinitionMiniAppModule.class.getName(), jsonObj.optJSONArray(MODULE));
		this.m_processes = HAPSerializeUtility.buildMapFromJsonObject(HAPDefinitionProcess.class.getName(), jsonObj.optJSONObject(PROCESS));
		return true;
	}	
}
