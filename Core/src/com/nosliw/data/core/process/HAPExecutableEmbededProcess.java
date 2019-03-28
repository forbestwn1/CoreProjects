package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;

//process that is part of system
//it should include data association that mapping result to system context 
public class HAPExecutableEmbededProcess extends HAPExecutableProcess{

	@HAPAttribute
	public static String OUTPUTMAPPING = "outputMapping";

	private Map<String, HAPExecutableDataAssociation> m_outputMapping;
	
	public HAPExecutableEmbededProcess(HAPDefinitionEmbededProcess definition, String id) {
		super(definition, id);
		this.m_outputMapping = new LinkedHashMap<String, HAPExecutableDataAssociation>();
	}

	public HAPDefinitionEmbededProcess getEmbededProcessDefinition() {   return (HAPDefinitionEmbededProcess)this.getDefinition();   }
	
	public void addBackToGlobalContext(String result, HAPExecutableDataAssociation backToGlobalContext) {   this.m_outputMapping.put(result, backToGlobalContext);  }

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		if(!this.m_outputMapping.isEmpty()) {
			Map<String, String> activityJsonMap = new LinkedHashMap<String, String>();
			for(String key : this.m_outputMapping.keySet()) {
				activityJsonMap.put(key, this.m_outputMapping.get(key).toResourceData(runtimeInfo).toString());
			}
			jsonMap.put(OUTPUTMAPPING, HAPJsonUtility.buildMapJson(activityJsonMap));
		}
	}
}
