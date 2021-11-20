package com.nosliw.data.core.complex;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.complex.valuestructure.HAPWrapperValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

//all value structure infor in domain
//  all value structure definition
//  all value structure runtime
public class HAPDomainValueStructure {

	//value structure definitions by id
	private Map<String, HAPWrapperValueStructure> m_valueStructure;
	
	//value structure definition id by value structure runtime id 
	private Map<String, String> m_definitionIdByRuntimeId;

	//id generator
	private HAPGeneratorId m_idGenerator;

	public HAPDomainValueStructure(HAPGeneratorId idGenerator) {
		this.m_idGenerator = idGenerator;
		this.m_valueStructure = new LinkedHashMap<String, HAPWrapperValueStructure>();
		this.m_definitionIdByRuntimeId = new LinkedHashMap<String, String>();
	}

	//add definition and create runtime id
	//return runtime id
	public String newRuntime(HAPValueStructure valueStructure) {
		String definitionId = this.addValueStructureDefinition(valueStructure);
		return this.newRuntime(definitionId);
	}
	
	//create another runtime that has common definition
	//return new runtime id
	public String cloneRuntime(String runtimeId) {
		String definitionId = this.m_definitionIdByRuntimeId.get(runtimeId);
		return this.newRuntime(definitionId);
	}
	
	public HAPValueStructure getValueStructureByRuntimeId(String runtimeId) {
		return this.getValueStructureWrapperByRuntimeId(runtimeId).getValueStructure();
	}
	
	public HAPWrapperValueStructure getValueStructureWrapperByRuntimeId(String runtimeId) {
		String definitionId = this.m_definitionIdByRuntimeId.get(runtimeId);
		return this.m_valueStructure.get(definitionId);
	}
	

	//create new runtime according to definition id 
	private String newRuntime(String definitionId) {
		String runtimeId = this.m_idGenerator.generateId();
		this.m_definitionIdByRuntimeId.put(runtimeId, definitionId);
		return runtimeId;
	}
	
	private String addValueStructureDefinition(HAPValueStructure valueStructure) {
		String id = this.m_idGenerator.generateId();
		HAPWrapperValueStructure wrapper = new HAPWrapperValueStructure(valueStructure);
		wrapper.setId(id);
		this.m_valueStructure.put(id, wrapper);
		return id;
	}
	
}
