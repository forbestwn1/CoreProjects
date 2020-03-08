package com.nosliw.data.core.process;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoUtility;
import com.nosliw.common.info.HAPEntityInfoWritable;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfoWrapper;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;

@HAPEntityWithAttribute
public class HAPExecutableResultActivityNormal extends HAPExecutableImpEntityInfoWrapper{

	@HAPAttribute
	public static String FLOW = "flow";

	@HAPAttribute
	public static String DATAASSOCIATION = "dataAssociation";

	private HAPExecutableDataAssociation m_dataAssociation;
	
	private HAPDefinitionResultActivityNormal m_definition;
	
	//next activity
	public HAPExecutableResultActivityNormal(HAPDefinitionResultActivityNormal definition) {
		super(definition);
		this.m_definition = definition;
	}
	
	public HAPDefinitionSequenceFlow getFlow() {  return this.m_definition.getFlow();  }
	
	public HAPExecutableDataAssociation getDataAssociation() {   return this.m_dataAssociation;  }
	public void setDataAssociation(HAPExecutableDataAssociation dataAssociation) {   this.m_dataAssociation = dataAssociation;   }

	@Override
	public HAPExecutableResultActivityNormal clone() {
		throw new RuntimeException();
	}

	@Override
	public void cloneToEntityInfo(HAPEntityInfoWritable entityInfo) {
		HAPEntityInfoUtility.cloneTo(this, entityInfo);
	}

	@Override
	public void buildEntityInfoByJson(Object json) {	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		HAPEntityInfoUtility.buildJsonMap(jsonMap, this);
		if(this.m_dataAssociation!=null)  jsonMap.put(DATAASSOCIATION, this.m_dataAssociation.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(FLOW, this.getFlow().toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		if(this.m_dataAssociation!=null)	jsonMap.put(DATAASSOCIATION, this.m_dataAssociation.toResourceData(runtimeInfo).toString());
		jsonMap.put(FLOW, this.getFlow().toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceDependency(dependency, runtimeInfo);
	}
}
