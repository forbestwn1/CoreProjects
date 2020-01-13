package com.nosliw.uiresource.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImpWrapper;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.HAPHandlerEvent;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;

@HAPEntityWithAttribute
public class HAPExecutableEventHandler  extends HAPEntityInfoImpWrapper implements HAPExecutable{

	@HAPAttribute
	public static String PROCESS = "process";
	
	private HAPExecutableWrapperTask<HAPExecutableProcess> m_process;

	public HAPExecutableWrapperTask<HAPExecutableProcess> getProcess() {   return this.m_process;   }
	
	
	public HAPExecutableEventHandler(HAPHandlerEvent eventHandlerDefinition) {
		super(eventHandlerDefinition);
	}

	public HAPExecutableEventHandler(HAPDefinitionEventHandler eventHandlerDefinition) {
		super(eventHandlerDefinition);
	}

	public void setProcess(HAPExecutableWrapperTask<HAPExecutableProcess> process) {   this.m_process = process;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PROCESS, HAPJsonUtility.buildJson(this.m_process, HAPSerializationFormat.JSON));
	}

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PROCESS, this.m_process.toResourceData(runtimeInfo).toString());
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.addAll(this.m_process.getResourceDependency(runtimeInfo));
		return out;
	}

}
