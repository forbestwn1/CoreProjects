package com.nosliw.data.core.script.context.dataassociation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceUtility;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPExecutableDataAssociationGroupWithTarget extends HAPExecutableDataAssociationGroup{

	@HAPAttribute
	public static String OUTPUTMATCHERS = "outputMatchers";

	//match from data association output to target context variable
	private Map<String, HAPMatchers> m_outputMatchers;

	public HAPExecutableDataAssociationGroupWithTarget(HAPDefinitionDataAssociationGroup definition) {
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
	protected void buildResourceDependency(List<HAPResourceDependent> dependency, HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceId> ids = new ArrayList<HAPResourceId>();
		for(String name : this.m_outputMatchers.keySet()) {
			ids.addAll(HAPMatcherUtility.getMatchersResourceId(this.m_outputMatchers.get(name)));
		}
		dependency.addAll(HAPResourceUtility.buildResourceDependentFromResourceId(ids));
	}

}
