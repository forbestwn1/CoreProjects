package com.nosliw.data.core.process;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPEntityInfoUtility;
import com.nosliw.common.info.HAPEntityInfoWritable;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;

@HAPEntityWithAttribute
public class HAPExecutableResultActivityNormal extends HAPExecutableImp implements HAPEntityInfo{

	@HAPAttribute
	public static String FLOW = "flow";

	@HAPAttribute
	public static String DATAASSOCIATION = "dataAssociation";

	private HAPExecutableDataAssociation m_dataAssociation;
	
	private HAPDefinitionResultActivityNormal m_definition;
	
	//next activity
	public HAPExecutableResultActivityNormal(HAPDefinitionResultActivityNormal definition) {
		this.m_definition = definition;
	}
	
	@Override
	public HAPInfo getInfo() {  return this.m_definition.getInfo(); }

	@Override
	public String getName() {  return this.m_definition.getName();  }

	@Override
	public String getDescription() {  return this.m_definition.getDescription();  }

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
