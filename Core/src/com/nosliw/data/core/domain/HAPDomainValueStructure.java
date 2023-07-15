package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;

//all value structure infor in domain
//  all value structure definition
//  all value structure runtime
@HAPEntityWithAttribute
public class HAPDomainValueStructure extends HAPSerializableImp{

	@HAPAttribute
	public static final String VALUESTRUCTURE = "valueStructure";

	@HAPAttribute
	public static final String DEFINITIONBYRUNTIME = "definitionByRuntime";

	//value structure definitions by id
	private Map<String, HAPInfoValueStructure> m_valueStructure;
	
	//value structure definition id by value structure runtime id 
	private Map<String, String> m_definitionIdByRuntimeId;

	//id generator
	private HAPGeneratorId m_idGenerator;

	private boolean m_isDirty;
	
	public HAPDomainValueStructure() {
		this.m_idGenerator = new HAPGeneratorId();
		this.m_valueStructure = new LinkedHashMap<String, HAPInfoValueStructure>();
		this.m_definitionIdByRuntimeId = new LinkedHashMap<String, String>();
		this.m_isDirty = false;
	}

	public void setIsDirty(boolean isDirty) {    this.m_isDirty = isDirty;     }
	public boolean getIsDirty() {     return this.m_isDirty;   }
	
	public HAPInfoValueStructure getValueStructureDefInfoByRuntimeId(String runtimeId) {	return getValueStructureDefinitionInfo(getValueStructureDefinitionIdByRuntimeId(runtimeId));	}
	public HAPDefinitionEntityValueStructure getValueStructureDefinitionByRuntimeId(String runtimeId) {	return getValueStructureDefInfoByRuntimeId(runtimeId).getValueStructure();	}
	public HAPDefinitionEntityValueStructure getValueStructure(String valueStructureDefId) {    return getValueStructureDefinitionInfo(valueStructureDefId).getValueStructure();     }
	public HAPInfoValueStructure getValueStructureDefinitionInfo(String valueStructureDefId) {    return this.m_valueStructure.get(valueStructureDefId);     }
	public String getValueStructureDefinitionIdByRuntimeId(String runtimeId) {	return this.m_definitionIdByRuntimeId.get(runtimeId);	}
	
	//create another runtime that has common definition
	//return new runtime id
	public String cloneRuntime(String runtimeId) {
		String definitionId = this.m_definitionIdByRuntimeId.get(runtimeId);
		return this.newRuntime(definitionId);
	}

	public String newValueStructure() {
		HAPDefinitionEntityValueStructure valueStructureEntityDef = new HAPDefinitionEntityValueStructure();
		String defId = this.m_idGenerator.generateId();
		this.m_valueStructure.put(defId, new HAPInfoValueStructure(valueStructureEntityDef, null));
		return this.newRuntime(defId);
	}
	
	//add definition and create runtime id
	//return runtime id
	public String newValueStructure(HAPInfoEntityInDomainDefinition valueStructureDefInfo, String valueStructureDefId) {
		String defId = valueStructureDefId;
		if(defId==null)	defId = this.m_idGenerator.generateId();
		this.m_valueStructure.put(defId, new HAPInfoValueStructure((HAPDefinitionEntityValueStructure)valueStructureDefInfo.getEntity().cloneEntityDefinitionInDomain(), valueStructureDefInfo.getExtraInfo().cloneExtraInfo()));
		return this.newRuntime(defId);
	}
	
	//create new runtime according to definition id 
	private String newRuntime(String definitionId) {
		String runtimeId = this.m_idGenerator.generateId();
		this.m_definitionIdByRuntimeId.put(runtimeId, definitionId);
		return runtimeId;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		Map<String, String> valueStructureJson = new LinkedHashMap<String, String>();
		for(String id : this.m_valueStructure.keySet()) {
			valueStructureJson.put(id, this.m_valueStructure.get(id).toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(VALUESTRUCTURE, HAPUtilityJson.buildMapJson(valueStructureJson));
		
		jsonMap.put(DEFINITIONBYRUNTIME, HAPUtilityJson.buildMapJson(this.m_definitionIdByRuntimeId));
	}
}
