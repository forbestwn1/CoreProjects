package com.nosliw.data.core.process;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociationGroupWithTarget;

@HAPEntityWithAttribute
public class HAPExecutableResultActivityNormal extends HAPExecutableDataAssociationGroupWithTarget{

	@HAPAttribute
	public static String FLOW = "flow";
 
	@HAPAttribute
//	public static String OUTPUTASSOCIATION = "outputAssociation";
	
	private HAPDefinitionResultActivityNormal m_definition;
	
	//associate output of activity to variable in process 
//	private HAPExecutableDataAssociationGroupWithTarget m_outputAssociation;
	
	//next activity
	public HAPExecutableResultActivityNormal(HAPDefinitionResultActivityNormal definition) {
		super(definition.getOutputDataAssociation());
		this.m_definition = definition;
	}
	
	public HAPDefinitionSequenceFlow getFlow() {  return this.m_definition.getFlow();  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(FLOW, this.getFlow().toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(FLOW, this.getFlow().toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependent> dependency, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceDependency(dependency, runtimeInfo);
	}
}
