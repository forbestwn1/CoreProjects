package com.nosliw.data.core.process.plugin;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPScript;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.process.HAPContextProcessor;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorActivity;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;

public class HAPPluginActivityImp implements HAPPluginActivity{

	@HAPAttribute
	public static String DEFINITION = "definition";
	
	@HAPAttribute
	public static String PROCESSOR = "processor";

	private String m_type;
	
	private Class<?> m_activityClass;
	
	private HAPProcessorActivity m_processor;
	
	private Map<String, HAPScript> m_scripts;
	
	public HAPPluginActivityImp() {
		this.m_scripts = new LinkedHashMap<String, HAPScript>();
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
		this.m_scripts.put(type, new HAPScript(script));
	}
	
	@Override
	public HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition,
			String id,
			HAPContextProcessor processContext,
			HAPExecutableProcess processExe, 
			HAPContextGroup processDataContext,
			Map<String, HAPExecutableDataAssociation> results,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processManager,
			HAPRequirementContextProcessor contextProcessRequirement, 
			HAPConfigureContextProcessor configure, 
			HAPProcessTracker processTracker) {
		return this.m_processor.process(activityDefinition, id, processContext, processExe, processDataContext, results, serviceProviders, processManager, contextProcessRequirement, configure, processTracker);
	}

	@Override
	public HAPDefinitionActivity buildActivityDefinition(Object obj) {
		HAPDefinitionActivity out = null;
		try {
			out = (HAPDefinitionActivity)this.m_activityClass.getConstructor(String.class).newInstance(this.getType());
			out.buildObject(obj, HAPSerializationFormat.JSON);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	@Override
	public HAPScript getScript(String env) {
		return this.m_scripts.get(env);
	}

}
