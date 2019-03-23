package com.nosliw.uiresource.application;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImpWrapper;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.process.HAPExecutableEmbededProcess;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPResourceData;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextStructure;

@HAPEntityWithAttribute
public class HAPExecutableAppEntry extends HAPEntityInfoImpWrapper implements HAPExecutable{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String UIMODULE = "uiModule";

	@HAPAttribute
	public static String CONTEXT = "context";

	@HAPAttribute
	public static String PROCESS = "process";
	
	@HAPAttribute
	public static String DATADEFINITION = "dataDefinition";
	  
	private String m_id;
	
	private Map<String, HAPExecutableAppModule> m_uiModules;

	//processes (used for lifecycle, module command)
	private Map<String, HAPExecutableEmbededProcess> m_processes;

	private HAPContextGroup m_context;
	
	private Map<String, HAPContext> m_dataDefinition;

	public HAPExecutableAppEntry(String entryName, HAPDefinitionApp appDef) {
		super(appDef.getEntry(entryName));
		this.m_processes = new LinkedHashMap<String, HAPExecutableEmbededProcess>();
		this.m_uiModules = new LinkedHashMap<String, HAPExecutableAppModule>();
		this.m_dataDefinition = new LinkedHashMap<String, HAPContext>();
	}

	public String getId() {  return this.m_id;   }
	public void setId(String id) {  this.m_id = id;  }
	
	public HAPContextGroup getContext() {   return this.m_context;   }
	public void setContext(HAPContextGroup context) {   this.m_context = context;  }

	public void addDataDefinition(String dataName, HAPContext dataDef) {  this.m_dataDefinition.put(dataName, dataDef);   }
	public Map<String, HAPContextStructure> getExtraContext(){  
		Map<String, HAPContextStructure> out = new LinkedHashMap<String, HAPContextStructure>();
		for(String dataName : this.m_dataDefinition.keySet()) {
			out.put("appdata_"+dataName, this.m_dataDefinition.get(dataName));
		}
		return out;
	}
	
	public void addUIModule(String name, HAPExecutableAppModule uiModuleInstance) {		this.m_uiModules.put(name, uiModuleInstance);	}
	public HAPExecutableAppModule getUIModuleInstance(String moduleName) {  return this.m_uiModules.get(moduleName);  }
	
	public void addProcess(String name, HAPExecutableEmbededProcess process) {		this.m_processes.put(name, process);	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.m_id);
		jsonMap.put(UIMODULE, HAPSerializeManager.getInstance().toStringValue(this.m_uiModules, HAPSerializationFormat.JSON));
		jsonMap.put(CONTEXT, HAPSerializeManager.getInstance().toStringValue(this.m_context, HAPSerializationFormat.JSON));
		jsonMap.put(PROCESS, HAPSerializeManager.getInstance().toStringValue(this.m_processes, HAPSerializationFormat.JSON));
		jsonMap.put(DATADEFINITION, HAPSerializeManager.getInstance().toStringValue(this.m_dataDefinition, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildFullJsonMap(jsonMap, typeJsonMap);

		Map<String, String> jsonModuleMap = new LinkedHashMap<String, String>();
		for(String name : this.m_uiModules.keySet()) {
			jsonModuleMap.put(name, this.m_uiModules.get(name).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(UIMODULE, HAPJsonUtility.buildMapJson(jsonModuleMap)); 

		Map<String, String> jsonProcessMap = new LinkedHashMap<String, String>();
		for(String name : this.m_processes.keySet()) {
			jsonProcessMap.put(name, this.m_processes.get(name).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(PROCESS, HAPJsonUtility.buildMapJson(jsonProcessMap)); 
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceDependent> out = new ArrayList<HAPResourceDependent>();
		for(String name : this.m_processes.keySet()) {
			out.addAll(this.m_processes.get(name).getResourceDependency(runtimeInfo));
		}
		
		for(String name : this.m_uiModules.keySet()) {
			out.addAll(this.m_uiModules.get(name).getResourceDependency(runtimeInfo));
		}
		return out;
	}
}
