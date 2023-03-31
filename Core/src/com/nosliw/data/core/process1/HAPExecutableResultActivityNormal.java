package com.nosliw.data.core.process1;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.entity.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.domain.entity.dataassociation.HAPParserDataAssociation;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPExecutableResultActivityNormal extends HAPExecutableImpEntityInfo{

	@HAPAttribute
	public static String FLOW = "flow";

	@HAPAttribute
	public static String DATAASSOCIATION = "dataAssociation";

	private HAPExecutableDataAssociation m_dataAssociation;
	
	private HAPDefinitionSequenceFlow m_flow;
	
//	private HAPDefinitionResultActivityNormal m_definition;

	public HAPExecutableResultActivityNormal() {}

	//next activity
	public HAPExecutableResultActivityNormal(HAPDefinitionResultActivityNormal definition) {
		super(definition);
		this.m_flow = definition.getFlow();
//		this.m_definition = definition;
	}
	
	public HAPDefinitionSequenceFlow getFlow() {  return this.m_flow;  }
	
	public HAPExecutableDataAssociation getDataAssociation() {   return this.m_dataAssociation;  }
	public void setDataAssociation(HAPExecutableDataAssociation dataAssociation) {   this.m_dataAssociation = dataAssociation;   }

	@Override
	public void cloneToEntityInfo(HAPEntityInfo entityInfo) {
		HAPUtilityEntityInfo.cloneTo(this, entityInfo);
	}

	@Override
	public void buildEntityInfoByJson(Object json) {	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_dataAssociation = HAPParserDataAssociation.buildExecutalbeByJson(jsonObj.getJSONObject(DATAASSOCIATION));
		this.m_flow = new HAPDefinitionSequenceFlow();
		this.m_flow.buildObject(jsonObj.getJSONObject(FLOW), HAPSerializationFormat.JSON);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		HAPUtilityEntityInfo.buildJsonMap(jsonMap, this);
		if(this.m_dataAssociation!=null)  jsonMap.put(DATAASSOCIATION, this.m_dataAssociation.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(FLOW, this.getFlow().toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		if(this.m_dataAssociation!=null)	jsonMap.put(DATAASSOCIATION, this.m_dataAssociation.toResourceData(runtimeInfo).toString());
		jsonMap.put(FLOW, this.getFlow().toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		super.buildResourceDependency(dependency, runtimeInfo, resourceManager);
	}
}
