package com.nosliw.data.core.component.valuestructure;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;

public class HAPValueStructureFlatInComponent extends HAPValueStructureDefinitionFlat implements HAPValueStructureInComponent{

	private List<HAPInfoReference> m_references;
	
	public HAPValueStructureFlatInComponent() {
		this.m_references = new ArrayList<HAPInfoReference>();
	}
	
	public void addReference(HAPInfoReference reference) {     this.m_references.add(reference);    }
	
	public List<HAPInfoReference> getReferences(){   return this.m_references;     }
}
