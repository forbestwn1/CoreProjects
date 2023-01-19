package com.nosliw.data.core.structure.reference;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.valuestructure.HAPExecutableEntityValueContext;

//a group of value structure complex for element reference
public class HAPCandidatesValueStructureComplex {

	private Map<String, HAPExecutableEntityValueContext> m_valueStructureComplexByName;

	public HAPCandidatesValueStructureComplex() {
		this.m_valueStructureComplexByName = new LinkedHashMap<String, HAPExecutableEntityValueContext>();
	}
	
	public HAPCandidatesValueStructureComplex(HAPExecutableEntityValueContext selfComplex, HAPExecutableEntityValueContext defaultComplex) {
		this();
		this.addSelf(selfComplex);
		this.addDefault(defaultComplex);
	}
	
	public void addSelf(HAPExecutableEntityValueContext valueStructureComplex) {   this.m_valueStructureComplexByName.put(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_SELF, valueStructureComplex);    }

	public void addDefault(HAPExecutableEntityValueContext valueStructureComplex) {   this.m_valueStructureComplexByName.put(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT, valueStructureComplex);    }

	public void addValueStructureComplex(String valueStructureComplexName, HAPExecutableEntityValueContext valueStructureComplex) {    this.m_valueStructureComplexByName.put(valueStructureComplexName, valueStructureComplex);     }

	public HAPExecutableEntityValueContext getValueStructureComplex(String name) {   return this.m_valueStructureComplexByName.get(name);    }

}
