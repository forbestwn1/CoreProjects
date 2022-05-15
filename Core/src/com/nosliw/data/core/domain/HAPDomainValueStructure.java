package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPExecutableEntityComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPPartComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPPartComplexValueStructureGroupWithEntity;
import com.nosliw.data.core.domain.entity.valuestructure.HAPPartComplexValueStructureSimple;
import com.nosliw.data.core.domain.entity.valuestructure.HAPUtilityComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPWrapperValueStructureDefinition;
import com.nosliw.data.core.domain.entity.valuestructure.HAPWrapperValueStructureExecutable;

//all value structure infor in domain
//  all value structure definition
//  all value structure runtime
public class HAPDomainValueStructure extends HAPSerializableImp{

	public static final String COMPLEX = "complex";

	public static final String VALUESTRUCTURE = "valueStructure";

	public static final String DEFINITIONBYRUNTIME = "definitionByRuntime";

	//value structure complex by id
	private Map<String, HAPExecutableEntityComplexValueStructure> m_valueStructureComplex;
	
	//value structure definitions by id
	private Map<String, HAPInfoValueStructure> m_valueStructure;
	
	//value structure definition id by value structure runtime id 
	private Map<String, String> m_definitionIdByRuntimeId;

	//id generator
	private HAPGeneratorId m_idGenerator;

	public HAPDomainValueStructure(HAPGeneratorId idGenerator) {
		this.m_idGenerator = idGenerator;
		this.m_valueStructure = new LinkedHashMap<String, HAPInfoValueStructure>();
		this.m_definitionIdByRuntimeId = new LinkedHashMap<String, String>();
		this.m_valueStructureComplex = new LinkedHashMap<String, HAPExecutableEntityComplexValueStructure>();
	}

	public String addValueStructureComplex(HAPDefinitionEntityComplexValueStructure valueStructureComplexDef, HAPDomainEntityDefinitionGlobal entityDefDomain, HAPDefinitionEntityContainerAttachment attachmentContainer) {
		HAPExecutableEntityComplexValueStructure valueStructureComplexExe = new HAPExecutableEntityComplexValueStructure();
		
		//extra value structure
		if(valueStructureComplexDef!=null) {
			for(HAPWrapperValueStructureDefinition part : valueStructureComplexDef.getParts()) {
				HAPInfoEntityInDomainDefinition valueStructureDefInfo = entityDefDomain.getSolidEntityInfoDefinition(part.getValueStructureId(), attachmentContainer);
				String valueStructureExeId = this.newValueStructure(valueStructureDefInfo, part.getValueStructureId().getEntityId());
				HAPWrapperValueStructureExecutable valueStructureWrapperExe = new HAPWrapperValueStructureExecutable(valueStructureExeId);
				valueStructureWrapperExe.cloneFromDefinition(part);
				valueStructureComplexExe.addPartSimple(valueStructureWrapperExe, HAPUtilityComplexValueStructure.createPartInfoDefault());
			}
		}

		String out = this.m_idGenerator.generateId();
		this.m_valueStructureComplex.put(out, valueStructureComplexExe);
		return out;
	}
	
	
	public HAPInfoValueStructure getValueStructureDefInfoByRuntimeId(String runtimeId) {	return getValueStructureDefinitionInfo(getValueStructureDefinitionIdByRuntimeId(runtimeId));	}
	public HAPDefinitionEntityValueStructure getValueStructureDefinitionByRuntimeId(String runtimeId) {	return getValueStructureDefInfoByRuntimeId(runtimeId).getValueStructure();	}
	public HAPDefinitionEntityValueStructure getValueStructure(String valueStructureDefId) {    return getValueStructureDefinitionInfo(valueStructureDefId).getValueStructure();     }
	public HAPInfoValueStructure getValueStructureDefinitionInfo(String valueStructureDefId) {    return this.m_valueStructure.get(valueStructureDefId);     }
	public String getValueStructureDefinitionIdByRuntimeId(String runtimeId) {	return this.m_definitionIdByRuntimeId.get(runtimeId);	}
	
	public HAPExecutableEntityComplexValueStructure getValueStructureComplex(String valueStructureComplexId) {	return this.m_valueStructureComplex.get(valueStructureComplexId);	}
	
	//create another runtime that has common definition
	//return new runtime id
	public String cloneRuntime(String runtimeId) {
		String definitionId = this.m_definitionIdByRuntimeId.get(runtimeId);
		return this.newRuntime(definitionId);
	}

	//extract value structure from complex and add to pool
	private void extractValueStructure(HAPPartComplexValueStructure part, HAPDomainEntityDefinitionSimpleResource entityDefDomain) {
		//part id
		part.setId(this.m_idGenerator.generateId());
		String partType = part.getPartType();
		if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_SIMPLE)) {
			HAPPartComplexValueStructureSimple simplePart = (HAPPartComplexValueStructureSimple)part;
			String valueStructureId = this.newValueStructure(simplePart.getValueStructure());
			simplePart.setValueStructureDefinitionId(valueStructureId);
		}
		else if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_GROUP_WITHENTITY)) {
			HAPPartComplexValueStructureGroupWithEntity entityGroup = (HAPPartComplexValueStructureGroupWithEntity)part;
			for(HAPPartComplexValueStructure child : entityGroup.getChildren()) {
				extractValueStructure(child, entityDefDomain);
			}
		}
	}

	//add definition and create runtime id
	//return runtime id
	private String newValueStructure(HAPInfoEntityInDomainDefinition valueStructureDefInfo, String valueStructureDefId) {
		String defId = valueStructureDefId;
		if(defId==null)	this.m_idGenerator.generateId();
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
		Map<String, String> complexJson = new LinkedHashMap<String, String>();
		for(String id : this.m_valueStructureComplex.keySet()) {
			complexJson.put(id, this.m_valueStructureComplex.get(id).toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(COMPLEX, HAPJsonUtility.buildMapJson(complexJson));
		
		Map<String, String> valueStructureJson = new LinkedHashMap<String, String>();
		for(String id : this.m_valueStructure.keySet()) {
			valueStructureJson.put(id, this.m_valueStructure.get(id).toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(VALUESTRUCTURE, HAPJsonUtility.buildMapJson(valueStructureJson));
		
		jsonMap.put(DEFINITIONBYRUNTIME, HAPJsonUtility.buildMapJson(this.m_definitionIdByRuntimeId));
	}

}
