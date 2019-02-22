package com.nosliw.uiresource.application;

import java.util.ArrayList;
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
import com.nosliw.data.core.script.context.HAPContextGroup;

@HAPEntityWithAttribute
public class HAPExecutableMiniAppEntry extends HAPEntityInfoImpWrapper implements HAPExecutable{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String UIMODULES = "uiModules";
	
	@HAPAttribute
	public static String PROCESS = "process";
	
	@HAPAttribute
	public static final String DATA = "data";
	  
	private String m_id;
	
	private Map<String, HAPExecutableMiniAppModule> m_uiModules;

	//processes (used for lifecycle, module command)
	private Map<String, HAPExecutableEmbededProcess> m_processes;

	private Map<String, List<HAPInstanceData>> m_data;
	
	private HAPContextGroup m_context;
	
	public HAPExecutableMiniAppEntry(String entryName, HAPDefinitionMiniApp appDef) {
		super(appDef.getEntry(entryName));
		this.m_data = new LinkedHashMap<String, List<HAPInstanceData>>();
		this.m_uiModules = new LinkedHashMap<String, HAPExecutableMiniAppModule>();
	}

	public String getId() {  return this.m_id;   }
	public void setId(String id) {  this.m_id = id;  }
	
	public HAPContextGroup getContext() {   return this.m_context;   }
	
	public void addUIModule(String name, HAPExecutableMiniAppModule uiModuleInstance) {		this.m_uiModules.put(name, uiModuleInstance);	}
	public HAPExecutableMiniAppModule getUIModuleInstance(String moduleName) {  return this.m_uiModules.get(moduleName);  }
	
	public void addProcess(String name, HAPExecutableEmbededProcess process) {		this.m_processes.put(name, process);	}

	public void addData(String dataName, HAPInstanceData data) {
		List<HAPInstanceData> dataByName = this.m_data.get(dataName);
		if(dataByName==null) {
			dataByName = new ArrayList<HAPInstanceData>();
			this.m_data.put(dataName, dataByName);
		}
		dataByName.add(data);
	}
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.m_id);
		jsonMap.put(UIMODULES, HAPSerializeManager.getInstance().toStringValue(this.m_uiModules, HAPSerializationFormat.JSON));
		jsonMap.put(DATA, HAPSerializeManager.getInstance().toStringValue(this.m_data, HAPSerializationFormat.JSON));
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
