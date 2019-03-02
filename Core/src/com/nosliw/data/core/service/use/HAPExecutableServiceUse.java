package com.nosliw.data.core.service.use;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociationWithTarget;

@HAPEntityWithAttribute
public class HAPExecutableServiceUse extends HAPExecutableImp{

	@HAPAttribute
	public static String PROVIDER = "provider";

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String INFO = "info";

	@HAPAttribute
	public static String PARMMAPPING = "parmMapping";

	@HAPAttribute
	public static String RESULTMAPPING = "resultMapping";

	//parms path
	private HAPExecutableDataAssociationWithTarget m_parmMapping;
	
	private Map<String, HAPExecutableDataAssociationWithTarget> m_resultMapping;
	
	private HAPDefinitionServiceUse m_definition;
	
	public HAPExecutableServiceUse(HAPDefinitionServiceUse definition) {
		this.m_definition = definition;
		this.m_resultMapping = new LinkedHashMap<String, HAPExecutableDataAssociationWithTarget>();
	}
	
	public void setParmMapping(HAPExecutableDataAssociationWithTarget parmMapping) {   this.m_parmMapping = parmMapping;   }
	
	public void addResultMapping(String result, HAPExecutableDataAssociationWithTarget mapping) {   this.m_resultMapping.put(result, mapping);  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PROVIDER, this.m_definition.getProvider());
		jsonMap.put(NAME, this.m_definition.getName());
		jsonMap.put(INFO, HAPJsonUtility.buildJson(this.m_definition.getInfo(), HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(PARMMAPPING, this.m_parmMapping.toResourceData(runtimeInfo).toString());
		
		Map<String, String> resultMap = new LinkedHashMap<String, String>();
		for(String result :this.m_resultMapping.keySet()) {
			resultMap.put(result, this.m_resultMapping.get(result).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(RESULTMAPPING, HAPJsonUtility.buildMapJson(resultMap));
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependent> dependency, HAPRuntimeInfo runtimeInfo) {
		if(this.m_parmMapping!=null)  dependency.addAll(this.m_parmMapping.getResourceDependency(runtimeInfo));  
		for(String result :this.m_resultMapping.keySet()) {
			dependency.addAll(this.m_resultMapping.get(result).getResourceDependency(runtimeInfo));
		}
	}
}
