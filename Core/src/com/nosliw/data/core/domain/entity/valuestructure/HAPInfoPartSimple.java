package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.List;

public class HAPInfoPartSimple {

	private HAPPartComplexValueStructureSimple m_simpleStructurePart;
	
	private List<Integer> m_priority;
	
	public HAPInfoPartSimple(HAPPartComplexValueStructureSimple simpleStructurePart) {
		this.m_simpleStructurePart = simpleStructurePart;
	}

	public HAPPartComplexValueStructureSimple getSimpleValueStructurePart() {	return this.m_simpleStructurePart;	}
	
	public List<Integer> getPriority(){   return this.m_priority;    }
	public void setPriority(List<Integer> priority) {     this.m_priority = priority;     }
}
