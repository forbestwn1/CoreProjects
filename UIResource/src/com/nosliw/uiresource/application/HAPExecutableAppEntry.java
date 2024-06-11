package com.nosliw.uiresource.application;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImpWrapper;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.process1.HAPExecutableProcess;
import com.nosliw.data.core.process1.resource.HAPResourceDefinitionProcess;
import com.nosliw.data.core.process1.resource.HAPResourceDefinitionProcessSuite;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.data.core.structure.temp.HAPUtilityContextScript;
import com.nosliw.data.core.valuestructure1.HAPValueStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionGroup;

@HAPEntityWithAttribute
public class HAPExecutableAppEntry extends HAPEntityInfoImpWrapper implements HAPExecutable{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String MODULE = "module";

	@HAPAttribute
	public static String CONTEXT = "context";

	@HAPAttribute
	public static String PROCESS = "process";
	
	@HAPAttribute
	public static String APPLICATIONDATA = "applicationData";

	@HAPAttribute
	public static String INITSCRIPT = "initScript";
	
	private String m_id;
	
	private Map<String, HAPExecutableAppModule> m_modules;
	//processes (used for lifecycle, module command)
	private Map<String, HAPExecutableWrapperTask<HAPExecutableProcess>> m_processes;

	private HAPValueStructureDefinitionGroup m_context;
	
	private Map<String, HAPDefinitionAppData> m_applicationData;

	private HAPDefinitionAppEntry m_definition;
	
	private HAPResourceDefinitionProcessSuite m_processSuite;
	
	public HAPExecutableAppEntry(HAPDefinitionAppEntry entryDef) {
		super(entryDef);
		this.m_processes = new LinkedHashMap<String, HAPExecutableWrapperTask<HAPExecutableProcess>>();
		this.m_modules = new LinkedHashMap<String, HAPExecutableAppModule>();
		this.m_applicationData = new LinkedHashMap<String, HAPDefinitionAppData>();
		this.m_definition = entryDef;
	}

	public HAPDefinitionAppEntry getDefinition() {   return this.m_definition;   }
	
	@Override
	public String getId() {  return this.m_id;   }
	@Override
	public void setId(String id) {  this.m_id = id;  }
	
	public HAPValueStructureDefinitionGroup getContext() {   return this.m_context;   }
	public void setContext(HAPValueStructureDefinitionGroup context) {   this.m_context = context;  }

	public void addApplicationData(String dataName, HAPDefinitionAppData dataDef) {  this.m_applicationData.put(dataName, dataDef);   }
	public Map<String, HAPValueStructure> getExtraContext(){  
		Map<String, HAPValueStructure> out = new LinkedHashMap<String, HAPValueStructure>();
		for(String dataName : this.m_applicationData.keySet()) {
			out.put(APPLICATIONDATA+"_"+dataName, this.m_applicationData.get(dataName));
		}
		return out;
	}
	
	public void addUIModule(String name, HAPExecutableAppModule uiModuleInstance) {		this.m_modules.put(name, uiModuleInstance);	}
	public HAPExecutableAppModule getUIModuleInstance(String moduleName) {  return this.m_modules.get(moduleName);  }
	
	public void addProcess(String name, HAPExecutableWrapperTask<HAPExecutableProcess> process) {		this.m_processes.put(name, process);	}

	public void setProcessSuite(HAPResourceDefinitionProcessSuite processSuite) {    this.m_processSuite = processSuite;    }
	
	public HAPResourceDefinitionProcess getProcessDefinition(String name) {    return new HAPResourceDefinitionProcess(this.m_processSuite, name);    }
	

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
		jsonMap.put(MODULE, HAPSerializeManager.getInstance().toStringValue(this.m_modules, HAPSerializationFormat.JSON));
		jsonMap.put(CONTEXT, HAPSerializeManager.getInstance().toStringValue(this.m_context, HAPSerializationFormat.JSON));
		jsonMap.put(PROCESS, HAPSerializeManager.getInstance().toStringValue(this.m_processes, HAPSerializationFormat.JSON));
		jsonMap.put(APPLICATIONDATA, HAPSerializeManager.getInstance().toStringValue(this.m_applicationData, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildFullJsonMap(jsonMap, typeJsonMap);

		Map<String, String> jsonModuleMap = new LinkedHashMap<String, String>();
		for(String name : this.m_modules.keySet()) {
			jsonModuleMap.put(name, this.m_modules.get(name).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(MODULE, HAPUtilityJson.buildMapJson(jsonModuleMap)); 

		Map<String, String> jsonProcessMap = new LinkedHashMap<String, String>();
		for(String name : this.m_processes.keySet()) {
			jsonProcessMap.put(name, this.m_processes.get(name).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(PROCESS, HAPUtilityJson.buildMapJson(jsonProcessMap)); 

		jsonMap.put(INITSCRIPT, HAPUtilityContextScript.buildValueStructureInitScript(this.getContext()).getScript());
		typeJsonMap.put(INITSCRIPT, HAPJsonTypeScript.class);

		return HAPResourceDataFactory.createJSValueResourceData(HAPUtilityJson.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		for(String name : this.m_processes.keySet()) {
			out.addAll(this.m_processes.get(name).getResourceDependency(runtimeInfo, resourceManager));
		}
		
		for(String name : this.m_modules.keySet()) {
			out.addAll(this.m_modules.get(name).getResourceDependency(runtimeInfo, resourceManager));
		}
		return out;
	}
}
