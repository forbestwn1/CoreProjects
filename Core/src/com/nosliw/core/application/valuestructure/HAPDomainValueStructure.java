package com.nosliw.core.application.valuestructure;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.core.application.common.structure.HAPRootInStructure;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;

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
	private Map<String, HAPDefinitionStructure> m_structureDefinition;
	
	//value structure runtime by id
	private Map<String, HAPInfoValueStructureRuntime> m_valueStructureRuntime;

	//value structure definition id by value structure runtime id 
	private Map<String, String> m_definitionIdByRuntimeId;

	//id generator
	private HAPGeneratorId m_idGenerator;

	private boolean m_isDirty;
	
	public HAPDomainValueStructure() {
		this.m_idGenerator = new HAPGeneratorId();
		this.m_structureDefinition = new LinkedHashMap<String, HAPDefinitionStructure>();
		this.m_valueStructureRuntime = new LinkedHashMap<String, HAPInfoValueStructureRuntime>();
		this.m_definitionIdByRuntimeId = new LinkedHashMap<String, String>();
		this.m_isDirty = false;
	}

	public void setIsDirty(boolean isDirty) {    this.m_isDirty = isDirty;     }
	public boolean getIsDirty() {     return this.m_isDirty;   }
	
	public HAPDefinitionStructure getStructureDefinitionByRuntimeId(String runtimeId) {	return getStructureDefinition(getStructureDefinitionIdByRuntimeId(runtimeId));	}
	public HAPDefinitionStructure getStructureDefinition(String structureDefId) {    return this.m_structureDefinition.get(structureDefId);     }

	public String getStructureDefinitionIdByRuntimeId(String runtimeId) {	return this.m_definitionIdByRuntimeId.get(runtimeId);	}
	
	public HAPInfoValueStructureRuntime getValueStructureRuntimeInfo(String runtimeId) {    return this.m_valueStructureRuntime.get(runtimeId);     }
	
	public Set<String> cleanupEmptyValueStructure() {
		Set<String> out = new HashSet<String>();
		
		Set<String> vsDefIds = new HashSet<String>();
		for(String vsDefId : this.m_structureDefinition.keySet()) {
			HAPDefinitionStructure vsDef = this.m_structureDefinition.get(vsDefId);
			if(vsDef.isEmpty()) {
				vsDefIds.add(vsDefId);
			}
		}

		for(String vsDefId : vsDefIds) {
			this.m_structureDefinition.remove(vsDefId);
		}
		
		for(String vsId : this.m_definitionIdByRuntimeId.keySet()) {
			if(vsDefIds.contains(this.m_definitionIdByRuntimeId.get(vsId))) {
				out.add(vsId);
			}
		}

		for(String vsId : out) {
			this.m_definitionIdByRuntimeId.remove(vsId);
			this.m_valueStructureRuntime.remove(vsId);
		}
		
		return out;
	}
	
	//create another runtime that has common definition
	//return new runtime id
	public String cloneRuntime(String runtimeId) {
		String definitionId = this.m_definitionIdByRuntimeId.get(runtimeId);
		String newDefId = cloneDefinition(definitionId);
		return this.newRuntime(newDefId, null, null, null);
	}

	public String newValueStructure() {
		String defId = this.m_idGenerator.generateId();
		this.m_structureDefinition.put(defId, new HAPDefinitionStructure());
		return this.newRuntime(defId, null, null, null);
	}
	
	//add definition and create runtime id
	//return runtime id
	public String newValueStructure(Set<HAPRootInStructure> roots, Object initValue, HAPInfo info, String name) {
		String id = this.m_idGenerator.generateId();
		this.m_structureDefinition.put(id, new HAPDefinitionStructure(roots));
		return this.newRuntime(id, initValue, info, name);
	}

	private String cloneDefinition(String defId) {
		HAPDefinitionStructure structureDef = this.getStructureDefinition(defId);
		String id = this.m_idGenerator.generateId();
		
		Set<HAPRootInStructure> roots = new HashSet<HAPRootInStructure>();
		for(HAPRootInStructure oldRoot : structureDef.getRoots()) {
			HAPRootInStructure newRoot = new HAPRootInStructure();
			oldRoot.cloneToEntityInfo(newRoot);
			newRoot.setDefinition(HAPUtilityStructure.solidateStructureElement(oldRoot.getDefinition()));
			roots.add(newRoot);
		}
		
		this.m_structureDefinition.put(id, new HAPDefinitionStructure(roots));
		return id;
	}
	
	//create new runtime according to definition id 
	private String newRuntime(String definitionId, Object initValue, HAPInfo info, String name) {
		String runtimeId = this.m_idGenerator.generateId();
		HAPInfoValueStructureRuntime runtimeValueStructureInfo = new HAPInfoValueStructureRuntime(runtimeId, info, name);
		runtimeValueStructureInfo.setInitValue(initValue);
		this.m_valueStructureRuntime.put(runtimeId, runtimeValueStructureInfo);
		
		this.m_definitionIdByRuntimeId.put(runtimeId, definitionId);
		return runtimeId;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		Map<String, String> valueStructureDefJson = new LinkedHashMap<String, String>();
		for(String id : this.m_structureDefinition.keySet()) {
			valueStructureDefJson.put(id, this.m_structureDefinition.get(id).toStringValue(HAPSerializationFormat.JSON));
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
