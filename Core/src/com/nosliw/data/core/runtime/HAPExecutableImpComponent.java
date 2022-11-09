package com.nosliw.data.core.runtime;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImpWrapper;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.component.HAPDefinitionEntityComponent;
import com.nosliw.data.core.process1.resource.HAPResourceDefinitionProcess;
import com.nosliw.data.core.process1.resource.HAPResourceDefinitionProcessSuite;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPExecutableImpComponent extends HAPEntityInfoImpWrapper implements HAPExecutable{

	@HAPAttribute
	public static String CONTEXT = "context";

	private HAPDefinitionEntityComponent m_component;
	
	// hook up with real data during runtime
	private HAPValueStructure m_context;

	private HAPResourceDefinitionProcessSuite m_processSuite;

	private Map<String, HAPDefinitionServiceProvider> m_serviceProviders;
	
	public HAPExecutableImpComponent(HAPDefinitionEntityComponent component) {
		super(component);
		this.m_component = component;
	}

	public HAPDefinitionEntityComponent getDefinition() {   return this.m_component;    }
	
	public HAPValueStructure getContextStructure() {   return this.m_context;   }
	public void setContextStructure(HAPValueStructure context) { 	this.m_context = context;	}

	public void setProcessSuite(HAPResourceDefinitionProcessSuite processSuite) {    this.m_processSuite = processSuite;    }
	public HAPResourceDefinitionProcess getProcessDefinition(String name) {    return new HAPResourceDefinitionProcess(this.m_processSuite, name);    }

	public void setServiceProviders(Map<String, HAPDefinitionServiceProvider> serviceProviders) {    this.m_serviceProviders = serviceProviders;    }
	public Map<String, HAPDefinitionServiceProvider> getServiceProviders(){    return this.m_serviceProviders;     }
	
	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		this.buildResourceDependency(out, runtimeInfo);
		return out;
	}

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		this.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		return HAPResourceDataFactory.createJSValueResourceData(HAPUtilityJson.buildMapJson(jsonMap, typeJsonMap));
	}

	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	}

	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {}

}
