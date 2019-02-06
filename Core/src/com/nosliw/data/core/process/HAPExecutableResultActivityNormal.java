package com.nosliw.data.core.process;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.HAPExecutableDataAssociationGroupWithTarget;

@HAPEntityWithAttribute
public class HAPExecutableResultActivityNormal extends HAPExecutableImp{

	@HAPAttribute
	public static String FLOW = "flow";
 
	@HAPAttribute
	public static String OUTPUTASSOCIATION = "outputAssociation";
	
	private HAPDefinitionResultActivityNormal m_definition;
	
	//associate output of activity to variable in process 
	private HAPExecutableDataAssociationGroupWithTarget m_outputAssociation;
	
	//next activity
	public HAPExecutableResultActivityNormal(HAPDefinitionResultActivityNormal definition) {
		this.m_definition = definition;
		this.m_outputAssociation = new HAPExecutableDataAssociationGroupWithTarget(this.m_definition.getOutputDataAssociation());
	}
	
	public HAPDefinitionSequenceFlow getFlow() {  return this.m_definition.getFlow();  }
	
	public HAPExecutableDataAssociationGroupWithTarget getOutputDataAssociation() {   return this.m_outputAssociation;   }
	public void setOutputDataAssociation(HAPExecutableDataAssociationGroupWithTarget output) {   this.m_outputAssociation = output;    }

	public void addOutputMatchers(String path, HAPMatchers matchers) {   this.m_outputAssociation.addOutputMatchers(path, matchers);     }
	public Map<String, HAPMatchers> getOutputMatchers() {  return this.m_outputAssociation.getOutputMatchers(); }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(FLOW, this.getFlow().toStringValue(HAPSerializationFormat.JSON));
		if(this.m_outputAssociation!=null)		jsonMap.put(OUTPUTASSOCIATION, m_outputAssociation.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		jsonMap.put(FLOW, this.getFlow().toStringValue(HAPSerializationFormat.JSON));
		if(this.m_outputAssociation!=null)  jsonMap.put(OUTPUTASSOCIATION, m_outputAssociation.toResourceData(runtimeInfo).toString());
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependent> dependency, HAPRuntimeInfo runtimeInfo) {
		dependency.addAll(this.m_outputAssociation.getResourceDependency(runtimeInfo));
	}
}
