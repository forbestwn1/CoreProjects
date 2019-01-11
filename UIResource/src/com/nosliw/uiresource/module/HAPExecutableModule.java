package com.nosliw.uiresource.module;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImpWrapper;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPResourceData;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.HAPContextGroup;

@HAPEntityWithAttribute
public class HAPExecutableModule extends HAPEntityInfoImpWrapper implements HAPExecutable{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String CONTEXT = "context";
	
	private HAPDefinitionModule m_moduleDefinition;
	
	private String m_id;

	// hook up with real data during runtime
	private HAPContextGroup m_contextGroup;

	//processes (used for lifecycle, module command)
	private Map<String, HAPExecutableProcess> m_processes;
	
	private Map<String, HAPExecutableModuleUI> m_uis;
	
	public HAPExecutableModule(HAPDefinitionModule moduleDefinition, String id) {
		super(moduleDefinition);
		this.m_moduleDefinition = moduleDefinition;
		this.m_id = id;
	}

	public void setContext(HAPContextGroup contextGroup) {   this.m_contextGroup = contextGroup;  }
	
	public HAPContextGroup getContext() {   return this.m_contextGroup;   }
	
	public void addProcess(String name, HAPExecutableProcess process) {		this.m_processes.put(name, process);	}
	
	public void addModuleUI(HAPExecutableModuleUI ui) {  this.m_uis.put(ui.getName(), ui);   }
	
	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

}
