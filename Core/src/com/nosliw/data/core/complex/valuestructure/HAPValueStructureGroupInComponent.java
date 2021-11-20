package com.nosliw.data.core.complex.valuestructure;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPValueStructureGroupInComponent extends HAPValueStructureDefinitionGroup implements HAPValueStructureInComponent{

	private Map<String, List<HAPInfoEntityReference>> m_references;
	
	public HAPValueStructureGroupInComponent() {
		this.m_references = new LinkedHashMap<String, List<HAPInfoEntityReference>>();
	}
	
	public void addReference(String categary, HAPInfoEntityReference reference) {
		List<HAPInfoEntityReference> refs = this.m_references.get(categary);
		if(refs==null) {
			refs = new ArrayList<HAPInfoEntityReference>();
			this.m_references.put(categary, refs);
		}
		refs.add(reference);
	}

	public Map<String, List<HAPInfoEntityReference>> getReferences(){    return this.m_references;     }
	
}
