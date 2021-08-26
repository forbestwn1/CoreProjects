package com.nosliw.data.core.component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.data.core.service.use.HAPExecutableServiceUse;
import com.nosliw.data.core.task.HAPExecutableTaskSuite;
import com.nosliw.data.core.valuestructure.HAPExecutableValueStructure;
import com.nosliw.data.core.valuestructure.HAPUtilityValueStructure;
import com.nosliw.data.core.valuestructure.HAPUtilityValueStructureScript;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPWrapperValueStructure;

public class HAPExecutableComponent extends HAPExecutableImpEntityInfo{

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";
	
	@HAPAttribute
	public static String INITSCRIPT = "initScript";

	@HAPAttribute
	public static String TASK = "task";

	@HAPAttribute
	public static final String SERVICE = "service";

	// hook up with real data during runtime
	private HAPWrapperValueStructure m_valueStructureWrapper;
	
	private HAPExecutableTaskSuite m_taskSuite;

	//service requirement definition
	private Map<String, HAPExecutableServiceUse> m_services;

	public HAPExecutableComponent(HAPDefinitionComponent componentDefinition, String id) {
		super(componentDefinition);
		this.setId(id);
		this.m_valueStructureWrapper = componentDefinition.getValueStructureWrapper();
		this.m_services = new LinkedHashMap<String, HAPExecutableServiceUse>();
	}

	public void addServiceUse(String name, HAPExecutableServiceUse serviceDef) {   this.m_services.put(name, serviceDef);   }
	public Map<String, HAPExecutableServiceUse> getServiceUses(){  return this.m_services;   }
	public HAPExecutableServiceUse getServiceUse(String name) {   return this.m_services.get(name);  }

	public HAPValueStructure getValueStructure() {   return this.m_valueStructureWrapper.getValueStructure();   }

	public HAPExecutableValueStructure getValueStructureExe() {
		return HAPUtilityValueStructure.buildExecuatableValueStructure(this.getValueStructure());
	}

	public void setValueStructure(HAPValueStructure valueStructure) { 	this.m_valueStructureWrapper.setValueStructure(valueStructure);	}
	
	public HAPExecutableTaskSuite getTaskSuite() {    return this.m_taskSuite;    }
	public void setTaskSuite(HAPExecutableTaskSuite taskSuite) {    this.m_taskSuite = taskSuite;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUESTRUCTURE, HAPJsonUtility.buildJson(this.m_valueStructureWrapper.getValueStructure(), HAPSerializationFormat.JSON));
		jsonMap.put(TASK, HAPJsonUtility.buildJson(this.m_taskSuite, HAPSerializationFormat.JSON));
		jsonMap.put(SERVICE, HAPJsonUtility.buildJson(this.m_services, HAPSerializationFormat.JSON));
	}
	
	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildFullJsonMap(jsonMap, typeJsonMap);

		jsonMap.put(INITSCRIPT, HAPUtilityValueStructureScript.buildValueStructureInitScript(this.getValueStructureExe()).getScript());
		typeJsonMap.put(INITSCRIPT, HAPJsonTypeScript.class);
		
		jsonMap.put(TASK, this.m_taskSuite.toResourceData(runtimeInfo).toString());
		
		Map<String, String> serviceResourceMap = new LinkedHashMap<String, String>();
		for(String serviceName : this.m_services.keySet()) 	serviceResourceMap.put(serviceName, this.m_services.get(serviceName).toResourceData(runtimeInfo).toString());
		jsonMap.put(SERVICE, HAPJsonUtility.buildMapJson(serviceResourceMap));
		
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.addAll(this.m_taskSuite.getResourceDependency(runtimeInfo, resourceManager));
		return out;
	}
}
