package com.nosliw.data.core.runtime;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPEntityInfoImpWrapper;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.data.core.script.context.HAPContextGroup;

public class HAPExecutableImpComponent extends HAPEntityInfoImpWrapper implements HAPExecutable{

	@HAPAttribute
	public static String CONTEXT = "context";

	// hook up with real data during runtime
	private HAPContextGroup m_context;

	private HAPDefinitionProcessSuite m_processSuite;

	public HAPExecutableImpComponent(HAPEntityInfo entityInfo) {
		super(entityInfo);
	}

	public HAPContextGroup getContext() {   return this.m_context;   }

	public void setContextGroup(HAPContextGroup contextGroup) { 	this.m_context = contextGroup;	}

	public void setProcessSuite(HAPDefinitionProcessSuite processSuite) {    this.m_processSuite = processSuite;    }
	
	public HAPDefinitionProcess getProcessDefinition(String name) {    return new HAPDefinitionProcess(this.m_processSuite, name);    }

	
	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
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
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}

	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	}

	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {}

}
