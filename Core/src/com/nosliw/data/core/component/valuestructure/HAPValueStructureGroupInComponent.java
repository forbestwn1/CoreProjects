package com.nosliw.data.core.component.valuestructure;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPValueStructureGroupInComponent extends HAPValueStructureDefinitionGroup implements HAPValueStructureInComponent{

	private Map<String, List<HAPInfoReference>> m_references;
	
	public HAPValueStructureGroupInComponent() {
		this.m_references = new LinkedHashMap<String, List<HAPInfoReference>>();
	}
	
	public void addReference(String categary, HAPInfoReference reference) {
		List<HAPInfoReference> refs = this.m_references.get(categary);
		if(refs==null) {
			refs = new ArrayList<HAPInfoReference>();
			this.m_references.put(categary, refs);
		}
		refs.add(reference);
	}

	public Map<String, List<HAPInfoReference>> getReferences(){    return this.m_references;     }
	
}
