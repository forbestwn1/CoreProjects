package com.nosliw.core.application.uitag;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.core.application.common.structure.HAPValueContextDefinition;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructure;

public class HAPUITagValueContextDefinition implements HAPValueContextDefinition{

	private List<HAPWrapperValueStructure> m_valueStructures;
	
	public HAPUITagValueContextDefinition() {
		this.m_valueStructures = new ArrayList<HAPWrapperValueStructure>();
	}
	
	
	@Override
	public List<HAPWrapperValueStructure> getValueStructures() {   return this.m_valueStructures;  }

}
