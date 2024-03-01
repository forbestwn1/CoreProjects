package com.nosliw.data.core.domain.valueport;

import java.util.List;

import com.nosliw.data.core.entity.division.manual.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.structure.reference.HAPInfoValueStructureReference;

public interface HAPValuePort {

	boolean isDefault();
	
	HAPIdValuePort getValuePortId();

	HAPInfoValuePort getValuePortInfo();
	
	List<HAPInfoValueStructureReference> discoverCandidateValueStructure(HAPReferenceValueStructure valueStructureRef);
	
	HAPDefinitionEntityValueStructure getValueStructureDefintion(String valueStructureId);

}
