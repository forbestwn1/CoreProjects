package com.nosliw.data.core.structure.reference;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.valuestructure.HAPExecutableEntityComplexValueStructure;

//a group of value structure complex for element reference
public class HAPCandidatesValueStructureComplex {

	private Map<String, HAPExecutableEntityComplexValueStructure> m_valueStructureComplexByName;

	public HAPCandidatesValueStructureComplex() {
		this.m_valueStructureComplexByName = new LinkedHashMap<String, HAPExecutableEntityComplexValueStructure>();
	}
	
	public HAPCandidatesValueStructureComplex(HAPExecutableEntityComplexValueStructure selfComplex, HAPExecutableEntityComplexValueStructure defaultComplex) {
		this();
		this.addSelf(selfComplex);
		this.addDefault(defaultComplex);
	}
	
	public void addSelf(HAPExecutableEntityComplexValueStructure valueStructureComplex) {   this.m_valueStructureComplexByName.put(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_SELF, valueStructureComplex);    }

	public void addDefault(HAPExecutableEntityComplexValueStructure valueStructureComplex) {   this.m_valueStructureComplexByName.put(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT, valueStructureComplex);    }

	public void addValueStructureComplex(String valueStructureComplexName, HAPExecutableEntityComplexValueStructure valueStructureComplex) {    this.m_valueStructureComplexByName.put(valueStructureComplexName, valueStructureComplex);     }

	public HAPExecutableEntityComplexValueStructure getValueStructureComplex(String name) {   return this.m_valueStructureComplexByName.get(name);    }

}
