package com.nosliw.core.application.brick.test.complex.script;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.data.variable.HAPIdVariable;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPExecutableVariableExpected extends HAPExecutableImp{

	@HAPAttribute
	public static String DEFINITION = "definition";
	
	@HAPAttribute
	public static String VARIABLEID = "variableId";
	
	@HAPAttribute
	public static String MATCHERS = "matchers";
	
	private HAPDefinitionVariableExpected m_definition;
	
	private HAPIdVariable m_variableId;
	
	private HAPMatchers m_matchers;
	
	public HAPExecutableVariableExpected(HAPDefinitionVariableExpected definition) {
		this.m_definition = definition;
	}

	public HAPDefinitionVariableExpected getDefinition() {     return this.m_definition;     }
	
	public HAPIdVariable getVariableId() {    return this.m_variableId;      }
	public void setVariableId(HAPIdVariable variableId) {    this.m_variableId = variableId;     }

	public void setMarchers(HAPMatchers matchers) {     this.m_matchers = matchers;      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VARIABLEID, this.m_variableId.toStringValue(HAPSerializationFormat.JSON));
		if(this.m_matchers!=null) jsonMap.put(MATCHERS, this.m_matchers.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		super.buildResourceDependency(dependency, runtimeInfo, resourceManager);
		if(this.m_matchers!=null) {
			dependency.addAll(this.m_matchers.getResourceDependency(runtimeInfo, resourceManager));
		}
	}
	
}
