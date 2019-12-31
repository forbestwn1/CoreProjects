package com.nosliw.uiresource.application;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.HAPComponentImp;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;

@HAPEntityWithAttribute
public class HAPDefinitionAppEntryUI  extends HAPComponentImp implements HAPDefinitionAppEntry{

	@HAPAttribute
	public static final String MODULE = "module";

	@HAPAttribute
	public static final String PROCESS = "process";

	//all modules in this entry
	private List<HAPDefinitionAppModule> m_modules;

	private Map<String, HAPDefinitionWrapperTask<HAPDefinitionProcess>> m_processes;

	public HAPDefinitionAppEntryUI(String id) {
		super(id);
		this.m_modules = new ArrayList<HAPDefinitionAppModule>();
		this.m_processes = new LinkedHashMap<String, HAPDefinitionWrapperTask<HAPDefinitionProcess>>();
	}
	
	public List<HAPDefinitionAppModule> getModules(){  return this.m_modules;  }
	public void addModules(List<HAPDefinitionAppModule> modules) {   if(modules!=null)   this.m_modules.addAll(modules);   }
	public void addModule(HAPDefinitionAppModule module) {  this.m_modules.add(module);  }
	
	public HAPDefinitionWrapperTask<HAPDefinitionProcess> getProcess(String name) {  return this.m_processes.get(name);   }
	public Map<String, HAPDefinitionWrapperTask<HAPDefinitionProcess>> getProcesses(){   return this.m_processes;  }
	public void addProcess(String name, HAPDefinitionWrapperTask<HAPDefinitionProcess> process) {  this.m_processes.put(name, process);    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(MODULE, HAPJsonUtility.buildJson(this.m_modules, HAPSerializationFormat.JSON));
		jsonMap.put(PROCESS, HAPJsonUtility.buildJson(this.m_processes, HAPSerializationFormat.JSON));
	}
}
