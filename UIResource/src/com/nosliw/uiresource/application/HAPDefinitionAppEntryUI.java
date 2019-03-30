package com.nosliw.uiresource.application;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;
import com.nosliw.uiresource.common.HAPComponentWithConfiguration;

@HAPEntityWithAttribute
public class HAPDefinitionAppEntryUI  extends HAPComponentWithConfiguration implements HAPDefinitionAppEntry{

	@HAPAttribute
	public static final String MODULE = "module";

	@HAPAttribute
	public static final String PROCESS = "process";

	@HAPAttribute
	public static final String CONTEXT = "context";

	//all modules in this entry
	private List<HAPDefinitionAppModule> m_modules;

	private Map<String, HAPDefinitionWrapperTask<HAPDefinitionProcess>> m_processes;

	//data structure shared by different module
	private HAPContextGroup m_context;
	
	public HAPDefinitionAppEntryUI() {
		this.m_modules = new ArrayList<HAPDefinitionAppModule>();
		this.m_processes = new LinkedHashMap<String, HAPDefinitionWrapperTask<HAPDefinitionProcess>>();
		this.m_context = new HAPContextGroup();
	}
	
	public List<HAPDefinitionAppModule> getModules(){  return this.m_modules;  }
	public void addModules(List<HAPDefinitionAppModule> modules) {   if(modules!=null)   this.m_modules.addAll(modules);   }
	
	public HAPDefinitionWrapperTask<HAPDefinitionProcess> getProcess(String name) {  return this.m_processes.get(name);   }
	public Map<String, HAPDefinitionWrapperTask<HAPDefinitionProcess>> getProcesses(){   return this.m_processes;  }
	public void addProcess(String name, HAPDefinitionWrapperTask<HAPDefinitionProcess> process) {  this.m_processes.put(name, process);    }
	
	public HAPContextGroup getContext() {
		if(this.m_context==null) {
			this.m_context = new HAPContextGroup();
		}
		return this.m_context;   
	}
	public void setContext(HAPContextGroup context) {   this.m_context = context;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(MODULE, HAPJsonUtility.buildJson(this.m_modules, HAPSerializationFormat.JSON));
		jsonMap.put(PROCESS, HAPJsonUtility.buildJson(this.m_processes, HAPSerializationFormat.JSON));
		jsonMap.put(CONTEXT, HAPJsonUtility.buildJson(this.m_context, HAPSerializationFormat.JSON));
	}
}
