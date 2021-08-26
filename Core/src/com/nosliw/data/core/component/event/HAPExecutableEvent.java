package com.nosliw.data.core.component.event;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.dataassociation.mapping.HAPExecutableDataAssociationMapping;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;

@HAPEntityWithAttribute
public class HAPExecutableEvent extends HAPExecutableImpEntityInfo{

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";

	@HAPAttribute
	public static String DATAASSOCIATION = "dataAssociation";

	//value structure
	private HAPValueStructureDefinitionFlat m_valueStructure;
	
	private HAPExecutableDataAssociationMapping m_dataAssociation;
	
	public HAPExecutableEvent() {}
	
	public HAPExecutableEvent(HAPValueStructureDefinitionFlat dataDefinition, HAPExecutableDataAssociationMapping dataAssociation) {
		this.m_valueStructure = dataDefinition;
		this.m_dataAssociation = dataAssociation;
	}

	public void setDataAssociation(HAPExecutableDataAssociationMapping dataAssociation) {    this.m_dataAssociation = dataAssociation;    }

	public void setValueStructure(HAPValueStructureDefinitionFlat valueStructure) {   this.m_valueStructure = valueStructure;    }
	public HAPValueStructureDefinitionFlat getValueStructure() {    return this.m_valueStructure;    }
	
	public HAPExecutableEvent cloneExeEvent() {
		HAPExecutableEvent out = new HAPExecutableEvent((HAPValueStructureDefinitionFlat)this.m_valueStructure.cloneStructure(), this.m_dataAssociation);
		this.cloneToEntityInfo(out);
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUESTRUCTURE, HAPJsonUtility.buildJson(this.m_valueStructure, HAPSerializationFormat.JSON));
		jsonMap.put(DATAASSOCIATION, HAPJsonUtility.buildJson(this.m_dataAssociation, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(DATAASSOCIATION, this.m_dataAssociation.toResourceData(runtimeInfo).toString());
	}
	
	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		dependency.addAll(this.m_dataAssociation.getResourceDependency(runtimeInfo, resourceManager));
	}
	
}
