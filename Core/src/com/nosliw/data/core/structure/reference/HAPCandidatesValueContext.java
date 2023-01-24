package com.nosliw.data.core.structure.reference;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.valuestructure.HAPExecutableEntityValueContext;

//a group of value structure complex for element reference
public class HAPCandidatesValueContext {

	private Map<String, HAPExecutableEntityValueContext> m_valueContextsByName;

	public HAPCandidatesValueContext() {
		this.m_valueContextsByName = new LinkedHashMap<String, HAPExecutableEntityValueContext>();
	}
	
	public HAPCandidatesValueContext(HAPExecutableEntityValueContext selfValueContext, HAPExecutableEntityValueContext defaultValueContext) {
		this();
		this.addSelf(selfValueContext);
		this.addDefault(defaultValueContext);
	}
	
	public void addSelf(HAPExecutableEntityValueContext valueContext) {   this.m_valueContextsByName.put(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_SELF, valueContext);    }

	public void addDefault(HAPExecutableEntityValueContext valueContext) {   this.m_valueContextsByName.put(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT, valueContext);    }

	public void addValueStructureComplex(String valueContextName, HAPExecutableEntityValueContext valueContext) {    this.m_valueContextsByName.put(valueContextName, valueContext);     }

	public HAPExecutableEntityValueContext getValueContext(String name) {   return this.m_valueContextsByName.get(name);    }

}
