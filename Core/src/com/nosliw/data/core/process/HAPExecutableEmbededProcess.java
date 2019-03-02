package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociationWithTarget;

//process that is part of system
//it should include data association that mapping result to system context 
public class HAPExecutableEmbededProcess extends HAPExecutableProcess{

	@HAPAttribute
	public static String BACKTOGLOBAL = "backToGlobal";

	private Map<String, HAPExecutableDataAssociationWithTarget> m_backToGlobals;
	
	public HAPExecutableEmbededProcess(HAPDefinitionEmbededProcess definition, String id) {
		super(definition, id);
		this.m_backToGlobals = new LinkedHashMap<String, HAPExecutableDataAssociationWithTarget>();
	}

	public HAPDefinitionEmbededProcess getEmbededProcessDefinition() {   return (HAPDefinitionEmbededProcess)this.getDefinition();   }
	
	public void addBackToGlobalContext(String result, HAPExecutableDataAssociationWithTarget backToGlobalContext) {   this.m_backToGlobals.put(result, backToGlobalContext);  }

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		Map<String, String> activityJsonMap = new LinkedHashMap<String, String>();
		for(String key : this.m_backToGlobals.keySet()) {
			activityJsonMap.put(key, this.m_backToGlobals.get(key).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(BACKTOGLOBAL, HAPJsonUtility.buildMapJson(activityJsonMap));
	}
}
