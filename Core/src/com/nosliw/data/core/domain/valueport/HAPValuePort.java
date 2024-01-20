package com.nosliw.data.core.domain.valueport;

import java.util.List;

import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.structure.reference.HAPInfoValueStructureReference;

public interface HAPValuePort {

	public HAPIdValuePort getValuePortId() {}

	public HAPInfoValuePort getValuePortInfo() {}
	
	public List<HAPInfoValueStructureReference> discoverCandidateValueStructure(HAPReferenceValueStructure valueStructureRef);
	
	public HAPDefinitionEntityValueStructure getValueStructureDefintion(String valueStructureId);

}
