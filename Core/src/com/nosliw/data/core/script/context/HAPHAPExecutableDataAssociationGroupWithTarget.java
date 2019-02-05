package com.nosliw.data.core.script.context;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPHAPExecutableDataAssociationGroupWithTarget extends HAPExecutableDataAssociationGroup{

	@HAPAttribute
	public static String OUTPUTMATCHERS = "outputMatchers";

	//match from data association output to target context variable
	private Map<String, HAPMatchers> m_outputMatchers;

	public HAPHAPExecutableDataAssociationGroupWithTarget(HAPDefinitionDataAssociationGroup definition) {
		super(definition);
	}
	
	public void addOutputMatchers(String path, HAPMatchers matchers) {   this.m_outputMatchers.put(path, matchers);     }
	public Map<String, HAPMatchers> getOutputMatchers() {  return this.m_outputMatchers; }
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(OUTPUTMATCHERS, HAPJsonUtility.buildJson(m_outputMatchers, HAPSerializationFormat.JSON));
	}
	
	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceDependent> out = super.getResourceDependency(runtimeInfo);
		//kkk add matchers resource here
		return out;  
		
	}
}
