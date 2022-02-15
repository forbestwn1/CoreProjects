package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;

public class HAPValueStructureInComplexFlat extends HAPValueStructureDefinitionFlat implements HAPValueStructureInComplex{

	private List<HAPInfoEntityReference> m_references;
	
	public HAPValueStructureInComplexFlat() {
		this.m_references = new ArrayList<HAPInfoEntityReference>();
	}
	
	public void addReference(HAPInfoEntityReference reference) {     this.m_references.add(reference);    }
	
	public List<HAPInfoEntityReference> getReferences(){   return this.m_references;     }
}
