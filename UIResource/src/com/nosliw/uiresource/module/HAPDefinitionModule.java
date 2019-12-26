package com.nosliw.uiresource.module;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.resource.external.HAPWithExternalMappingEntityInfoImp;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;
import com.nosliw.data.core.service.use.HAPDefinitionServiceInEntity;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPWithServiceProvider;
import com.nosliw.uiresource.common.HAPInfoDecoration;

/**
Module is a independent entity that is runnable within a container
It is a simple application which contains pages and transition between pages
Also be aware that module can be run in different container (pc browser, mobile browser, ...), try to make it platform independent
for instance, for a module that shows a school information, it contains two pages: list of school, school info
    when in mobile phone, the school info should overlap on top of list page
    when in pc, the list page and the school page should diplay sid by side

Don't need to define service information here, as service information will be gathered from all the mdoule ui definition
 */
@HAPEntityWithAttribute
public class HAPDefinitionModule extends HAPWithExternalMappingEntityInfoImp implements HAPWithServiceProvider{

	@HAPAttribute
	public static String CONTEXT = "context";
	
	@HAPAttribute
	public static String UI = "ui";
	
	@HAPAttribute
	public static String PROCESS = "process";

	@HAPAttribute
	public static String UIDECORATION = "uiDecoration";
	
	@HAPAttribute
	public static String SERVICE = "services";
	
	private String m_id;
	
	// data definition
	// hook up with real data during runtime
	private HAPContextGroup m_contextGroup;
	
	// all the module uis (name -- definition)
	private List<HAPDefinitionModuleUI> m_uis;

	//processes (used for lifecycle, module command)
	private Map<String, HAPDefinitionWrapperTask<HAPDefinitionProcess>> m_processes;

	private List<HAPInfoDecoration> m_uiDecoration;
	
	//service definition
	private HAPDefinitionServiceInEntity m_serviceDefinition;

	public HAPDefinitionModule(String id) {
		this.m_id = id;
		this.m_uis = new ArrayList<HAPDefinitionModuleUI>();
		this.m_processes = new LinkedHashMap<String, HAPDefinitionWrapperTask<HAPDefinitionProcess>>();
		this.m_uiDecoration = new ArrayList<HAPInfoDecoration>();
		this.m_serviceDefinition = new HAPDefinitionServiceInEntity();
	}
	
	@Override
	public Map<String, HAPDefinitionServiceProvider> getServiceProviderDefinitions(){  return this.m_serviceDefinition.getServiceProviderDefinitions();   }
	
	public String getId() {   return this.m_id;   }
	
	public HAPContextGroup getContext() {  return this.m_contextGroup;   }
	public void setContext(HAPContextGroup contextGroup) {  this.m_contextGroup = contextGroup;   }
	
	public Map<String, HAPDefinitionWrapperTask<HAPDefinitionProcess>> getProcesses(){  return this.m_processes;  }
	public void addProcess(HAPDefinitionWrapperTask<HAPDefinitionProcess> processDef) {  this.m_processes.put(processDef.getTaskDefinition().getName(), processDef);  }
	 
	public List<HAPDefinitionModuleUI> getUIs(){  return this.m_uis;  }
	public void addUI(HAPDefinitionModuleUI ui) {   this.m_uis.add(ui);   }
	
	public void setUIDecoration(List<HAPInfoDecoration> decs) {  this.m_uiDecoration = decs;    }
	public List<HAPInfoDecoration> getUIDecoration(){   return this.m_uiDecoration;    }
	
	public void addServiceUseDefinition(HAPDefinitionServiceUse def) {  this.m_serviceDefinition.addServiceUseDefinition(def);   }
	public void addServiceProviderDefinition(HAPDefinitionServiceProvider def) {  this.m_serviceDefinition.addServiceProviderDefinition(def);   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONTEXT, HAPJsonUtility.buildJson(this.m_contextGroup, HAPSerializationFormat.JSON));
		jsonMap.put(UI, HAPJsonUtility.buildJson(this.m_uis, HAPSerializationFormat.JSON));
		jsonMap.put(PROCESS, HAPJsonUtility.buildJson(this.m_processes, HAPSerializationFormat.JSON));
		jsonMap.put(UIDECORATION, HAPJsonUtility.buildJson(this.m_uiDecoration, HAPSerializationFormat.JSON));
	}

}
