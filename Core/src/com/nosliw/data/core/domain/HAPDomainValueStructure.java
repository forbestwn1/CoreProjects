package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPDefinitionEntityValueStructure;

//all value structure infor in domain
//  all value structure definition
//  all value structure runtime
@HAPEntityWithAttribute
public class HAPDomainValueStructure extends HAPSerializableImp{

	@HAPAttribute
	public static final String VALUESTRUCTUREDEFINITION = "valueStructureDefinition";

	@HAPAttribute
	public static final String VALUESTRUCTURERUNTIME = "valueStructureRuntime";

	@HAPAttribute
	public static final String DEFINITIONBYRUNTIME = "definitionByRuntime";

	//value structure definitions by id
	private Map<String, HAPInfoValueStructureDefinition> m_valueStructureDefinition;
	
	//value structure runtime by id
	private Map<String, HAPInfoValueStructureRuntime> m_valueStructureRuntime;

	//value structure definition id by value structure runtime id 
	private Map<String, String> m_definitionIdByRuntimeId;

	//id generator
	private HAPGeneratorId m_idGenerator;

	private boolean m_isDirty;
	
	public HAPDomainValueStructure() {
		this.m_idGenerator = new HAPGeneratorId();
		this.m_valueStructureDefinition = new LinkedHashMap<String, HAPInfoValueStructureDefinition>();
		this.m_valueStructureRuntime = new LinkedHashMap<String, HAPInfoValueStructureRuntime>();
		this.m_definitionIdByRuntimeId = new LinkedHashMap<String, String>();
		this.m_isDirty = false;
	}

	public void setIsDirty(boolean isDirty) {    this.m_isDirty = isDirty;     }
	public boolean getIsDirty() {     return this.m_isDirty;   }
	
	public HAPInfoValueStructureDefinition getValueStructureDefInfoByRuntimeId(String runtimeId) {	return getValueStructureDefinitionInfo(getValueStructureDefinitionIdByRuntimeId(runtimeId));	}
	public HAPDefinitionEntityValueStructure getValueStructureDefinitionByRuntimeId(String runtimeId) {	return getValueStructureDefInfoByRuntimeId(runtimeId).getValueStructure();	}
	public HAPDefinitionEntityValueStructure getValueStructure(String valueStructureDefId) {    return getValueStructureDefinitionInfo(valueStructureDefId).getValueStructure();     }
	public HAPInfoValueStructureDefinition getValueStructureDefinitionInfo(String valueStructureDefId) {    return this.m_valueStructureDefinition.get(valueStructureDefId);     }

	public String getValueStructureDefinitionIdByRuntimeId(String runtimeId) {	return this.m_definitionIdByRuntimeId.get(runtimeId);	}
	
	public HAPInfoValueStructureRuntime getValueStructureRuntimeInfo(String runtimeId) {    return this.m_valueStructureRuntime.get(runtimeId);     }
	
	
	//create another runtime that has common definition
	//return new runtime id
	public String cloneRuntime(String runtimeId) {
		String definitionId = this.m_definitionIdByRuntimeId.get(runtimeId);
		return this.newRuntime(definitionId, null, null);
	}

	public String newValueStructure() {
		HAPDefinitionEntityValueStructure valueStructureEntityDef = new HAPDefinitionEntityValueStructure();
		String defId = this.m_idGenerator.generateId();
		this.m_valueStructureDefinition.put(defId, new HAPInfoValueStructureDefinition(valueStructureEntityDef));
		return this.newRuntime(defId, null, null);
	}
	
	//add definition and create runtime id
	//return runtime id
	public String newValueStructure(HAPDefinitionEntityValueStructure valueStructureDef, HAPInfo info, String name) {
		String id = this.m_idGenerator.generateId();
		this.m_valueStructureDefinition.put(id, new HAPInfoValueStructureDefinition(valueStructureDef.cloneValueStructure()));
		return this.newRuntime(id, info, name);
	}

	//create new runtime according to definition id 
	private String newRuntime(String definitionId, HAPInfo info, String name) {
		String runtimeId = this.m_idGenerator.generateId();
		this.m_definitionIdByRuntimeId.put(runtimeId, definitionId);
		this.m_valueStructureRuntime.put(runtimeId, new HAPInfoValueStructureRuntime(runtimeId, info, name));
		return runtimeId;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		Map<String, String> valueStructureDefJson = new LinkedHashMap<String, String>();
		for(String id : this.m_valueStructureDefinition.keySet()) {
			valueStructureDefJson.put(id, this.m_valueStructureDefinition.get(id).toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(VALUESTRUCTUREDEFINITION, HAPUtilityJson.buildMapJson(valueStructureDefJson));
		
		Map<String, String> valueStructureRuntimeJson = new LinkedHashMap<String, String>();
		for(String id : this.m_valueStructureRuntime.keySet()) {
			valueStructureRuntimeJson.put(id, this.m_valueStructureRuntime.get(id).toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(VALUESTRUCTURERUNTIME, HAPUtilityJson.buildMapJson(valueStructureRuntimeJson));
		
		jsonMap.put(DEFINITIONBYRUNTIME, HAPUtilityJson.buildMapJson(this.m_definitionIdByRuntimeId));
	}
}
