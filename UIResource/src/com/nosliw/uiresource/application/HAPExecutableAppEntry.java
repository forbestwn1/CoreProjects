package com.nosliw.uiresource.application;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImpWrapper;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.process.HAPExecutableEmbededProcess;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPResourceData;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextGroup;

@HAPEntityWithAttribute
public class HAPExecutableAppEntry extends HAPEntityInfoImpWrapper implements HAPExecutable{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String UIMODULES = "uiModules";
	
	@HAPAttribute
	public static String PROCESS = "process";
	
	@HAPAttribute
	public static final String DATA = "data";
	  
	private String m_id;
	
	private Map<String, HAPExecutableAppModule> m_uiModules;

	//processes (used for lifecycle, module command)
	private Map<String, HAPExecutableEmbededProcess> m_processes;

	private HAPContextGroup m_context;
	
	private Map<String, HAPContext> m_dataDefinition;

	public HAPExecutableAppEntry(String entryName, HAPDefinitionApp appDef) {
		super(appDef.getEntry(entryName));
		this.m_uiModules = new LinkedHashMap<String, HAPExecutableAppModule>();
		this.m_dataDefinition = new LinkedHashMap<String, HAPContext>();
	}

	public String getId() {  return this.m_id;   }
	public void setId(String id) {  this.m_id = id;  }
	
	public HAPContextGroup getContext() {   return this.m_context;   }
	public void setContext(HAPContextGroup context) {   this.m_context = context;  }

	public void addDataDefinition(String dataName, HAPContext dataDef) {  this.m_dataDefinition.put(dataName, dataDef);   }
	public Map<String, HAPContext> getExtraContext(){  
		Map<String, HAPContext> out = new LinkedHashMap<String, HAPContext>();
		for(String dataName : this.m_dataDefinition.keySet()) {
			out.put("appdata."+dataName, this.m_dataDefinition.get(dataName));
		}
		return out;
	}
	
	public void addUIModule(String name, HAPExecutableAppModule uiModuleInstance) {		this.m_uiModules.put(name, uiModuleInstance);	}
	public HAPExecutableAppModule getUIModuleInstance(String moduleName) {  return this.m_uiModules.get(moduleName);  }
	
	public void addProcess(String name, HAPExecutableEmbededProcess process) {		this.m_processes.put(name, process);	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.m_id);
		jsonMap.put(UIMODULES, HAPSerializeManager.getInstance().toStringValue(this.m_uiModules, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}

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
