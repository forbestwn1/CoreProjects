package com.nosliw.uiresource.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.HAPExecutableComponent;
import com.nosliw.data.core.process1.resource.HAPResourceDefinitionProcess;
import com.nosliw.data.core.process1.resource.HAPResourceDefinitionProcessSuite;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPExecutableModule extends HAPExecutableComponent{

	@HAPAttribute
	public static String UI = "ui";
	 
//	@HAPAttribute
//	public static String PROCESS = "process";

//	@HAPAttribute
//	public static String LIFECYCLE = "lifecycle";

	private HAPDefinitionModule m_moduleDefinition;
	
	//processes (used for lifecycle, module command)
//	private Map<String, HAPExecutableWrapperTask<HAPExecutableProcess>> m_processes;
	
//	private Map<String, HAPExecutableWrapperTask<HAPExecutableProcess>> m_lifecycle;

	private List<HAPExecutableModuleUI> m_uis;

	private HAPResourceDefinitionProcessSuite m_processSuite;
	
	public HAPExecutableModule(HAPDefinitionModule moduleDefinition, String id) {
		super(moduleDefinition, id);
//		this.m_processes = new LinkedHashMap<String, HAPExecutableWrapperTask<HAPExecutableProcess>>();
//		this.m_lifecycle = new LinkedHashMap<String, HAPExecutableWrapperTask<HAPExecutableProcess>>();
		this.m_uis = new ArrayList<HAPExecutableModuleUI>();
		this.m_moduleDefinition = moduleDefinition;
	}

	public HAPDefinitionModule getDefinition() {   return this.m_moduleDefinition;  }
	
//	public void addProcess(String name, HAPExecutableWrapperTask<HAPExecutableProcess> process) {		this.m_processes.put(name, process);	}
//
//	public void addLifecycle(String name, HAPExecutableWrapperTask<HAPExecutableProcess> lifecycle) {		this.m_lifecycle.put(name, lifecycle);	}

	public void addModuleUI(HAPExecutableModuleUI ui) {  this.m_uis.add(ui);   }
	
	public void setProcessSuite(HAPResourceDefinitionProcessSuite processSuite) {    this.m_processSuite = processSuite;    }
	
	public HAPResourceDefinitionProcess getProcessDefinition(String name) {    return new HAPResourceDefinitionProcess(this.m_processSuite, name);    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(UI, HAPUtilityJson.buildJson(this.m_uis, HAPSerializationFormat.JSON));
//		jsonMap.put(PROCESS, HAPJsonUtility.buildJson(this.m_processes, HAPSerializationFormat.JSON));
//		jsonMap.put(LIFECYCLE, HAPJsonUtility.buildJson(this.m_lifecycle, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		
		List<String> uiJsonList = new ArrayList<String>();
		for(HAPExecutableModuleUI ui :this.m_uis) {
			uiJsonList.add(ui.toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(UI, HAPUtilityJson.buildArrayJson(uiJsonList.toArray(new String[0])));
		
//		Map<String, String> processJsonMap = new LinkedHashMap<String, String>();
//		for(String processName :this.m_processes.keySet()) {
//			processJsonMap.put(processName, this.m_processes.get(processName).toResourceData(runtimeInfo).toString());
//		}
//		jsonMap.put(PROCESS, HAPJsonUtility.buildMapJson(processJsonMap));
		
//		Map<String, String> lifecycleJsonMap = new LinkedHashMap<String, String>();
//		for(String lifecycleName :this.m_lifecycle.keySet()) {
//			lifecycleJsonMap.put(lifecycleName, this.m_lifecycle.get(lifecycleName).toResourceData(runtimeInfo).toString());
//		}
//		jsonMap.put(LIFECYCLE, HAPJsonUtility.buildMapJson(lifecycleJsonMap));
	}
	
//	@Override
//	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
//		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
//		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
//		this.buildFullJsonMap(jsonMap, typeJsonMap);
//
//		List<String> uiJsonList = new ArrayList<String>();
//		for(HAPExecutableModuleUI ui :this.m_uis) {
//			uiJsonList.add(ui.toResourceData(runtimeInfo).toString());
//		}
//		jsonMap.put(UI, HAPJsonUtility.buildArrayJson(uiJsonList.toArray(new String[0])));
//		
//		Map<String, String> processJsonMap = new LinkedHashMap<String, String>();
//		for(String processName :this.m_processes.keySet()) {
//			processJsonMap.put(processName, this.m_processes.get(processName).toResourceData(runtimeInfo).toString());
//		}
//		jsonMap.put(PROCESS, HAPJsonUtility.buildMapJson(processJsonMap));
//		
//		Map<String, String> lifecycleJsonMap = new LinkedHashMap<String, String>();
//		for(String lifecycleName :this.m_lifecycle.keySet()) {
//			lifecycleJsonMap.put(lifecycleName, this.m_lifecycle.get(lifecycleName).toResourceData(runtimeInfo).toString());
//		}
//		jsonMap.put(LIFECYCLE, HAPJsonUtility.buildMapJson(lifecycleJsonMap));
//
//		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
//	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.addAll(super.getResourceDependency(runtimeInfo, resourceManager));
		for(HAPExecutableModuleUI ui : this.m_uis) {		out.addAll(ui.getResourceDependency(runtimeInfo, resourceManager));		}
//		for(HAPExecutableWrapperTask process : this.m_processes.values()) {		out.addAll(process.getResourceDependency(runtimeInfo, resourceManager));		}
//		for(HAPExecutableWrapperTask lifecycle : this.m_lifecycle.values()) {	out.addAll(lifecycle.getResourceDependency(runtimeInfo, resourceManager));	}
		return out;
	}
}
