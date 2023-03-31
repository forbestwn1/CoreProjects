package com.nosliw.data.core.service.use;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.domain.entity.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

//data association between two service
@HAPEntityWithAttribute
public class HAPExecutableProviderToUse extends HAPExecutableImp{

	@HAPAttribute
	public static String PARMMAPPING = "parmMapping";

	@HAPAttribute
	public static String RESULTMAPPING = "resultMapping";

	//parm mapping from use to provider
	private HAPExecutableDataAssociation m_parmMapping;
	
	//result mapping from provider to use
	private Map<String, HAPExecutableDataAssociation> m_resultMapping;
	
	public HAPExecutableProviderToUse() {
		this.m_resultMapping = new LinkedHashMap<String, HAPExecutableDataAssociation>();
	}
	
	public HAPExecutableDataAssociation getParmMapping() {   return this.m_parmMapping;  }
	public void setParmMapping(HAPExecutableDataAssociation mapping) {   this.m_parmMapping = mapping;   }
	
	public Map<String, HAPExecutableDataAssociation> getResultMapping(){   return this.m_resultMapping;    } 
	public void addResultMapping(String result, HAPExecutableDataAssociation mapping) {    this.m_resultMapping.put(result, mapping);     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PARMMAPPING, HAPUtilityJson.buildJson(this.m_parmMapping, HAPSerializationFormat.JSON));
		jsonMap.put(RESULTMAPPING, HAPSerializeManager.getInstance().toStringValue(this.m_resultMapping, HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(PARMMAPPING, this.m_parmMapping.toResourceData(runtimeInfo).toString());
		Map<String, String> resultMapping = new LinkedHashMap<String, String>();
		for(String name : this.m_resultMapping.keySet()) {
			resultMapping.put(name, this.m_resultMapping.get(name).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(RESULTMAPPING, HAPUtilityJson.buildMapJson(resultMapping));
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		dependency.addAll(this.m_parmMapping.getResourceDependency(runtimeInfo, resourceManager));
		for(HAPExecutableDataAssociation mapping : this.m_resultMapping.values()) {
			dependency.addAll(mapping.getResourceDependency(runtimeInfo, resourceManager));
		}
	}

}
