package com.nosliw.data.core.activity;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.component.HAPDefinitionEntityComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.valuestructure.HAPWrapperValueStructure;

public class HAPPluginActivityImp implements HAPPluginActivity{

	@HAPAttribute
	public static String DEFINITION = "definition";
	
	@HAPAttribute
	public static String PROCESSOR = "processor";

	private String m_type;
	
	private Class<?> m_activityClass;
	
	private HAPProcessorActivity m_processor;
	
	private Map<String, HAPJsonTypeScript> m_scripts;
	
	public HAPPluginActivityImp() {
		this.m_scripts = new LinkedHashMap<String, HAPJsonTypeScript>();
	}
	
	@Override
	public String getType() {		return this.m_type;  	}
	public void setType(String type) {   this.m_type = type;   }

	public void setProcessorClass(String processorClass) {
		try {
			this.m_processor = (HAPProcessorActivity)Class.forName(processorClass).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setActivityClass(String activityClass) {
		try {
			this.m_activityClass = Class.forName(activityClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void addScript(String type, String script) {
		this.m_scripts.put(type, new HAPJsonTypeScript(script));
	}
	
	@Override
	public HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition, 
			String id,
			HAPContextProcessor processContext, 
			HAPWrapperValueStructure valueStructureWrapper,
			HAPRuntimeEnvironment runtimeEnv,
			HAPConfigureProcessorStructure configure, 
			HAPProcessTracker processTracker) {
		return this.m_processor.process(activityDefinition, id, processContext, valueStructureWrapper, runtimeEnv, configure, processTracker);
	}

	@Override
	public HAPDefinitionActivity buildActivityDefinition(Object obj, HAPDefinitionEntityComplex complexEntity) {
		HAPDefinitionActivity out = null;
		try {
			out = (HAPDefinitionActivity)this.m_activityClass.getConstructor(String.class).newInstance(this.getType());
			out.parseActivityDefinition(obj, complexEntity, HAPSerializationFormat.JSON);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	@Override
	public HAPJsonTypeScript getScript(String env) {
		return this.m_scripts.get(env);
	}

	@Override
	public HAPExecutableActivity buildActivityExecutable(Object obj) {
		throw new RuntimeException();
	}

}
