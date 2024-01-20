package com.nosliw.data.core.structure.reference;

import java.util.List;

import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.domain.valueport.HAPReferenceValueStructure;

public interface HAPContextStructureReference {

	List<HAPInfoValueStructureReference> discoverCandidateValueStructure(HAPReferenceValueStructure valueStructureRef);
	
	HAPDefinitionEntityValueStructure getValueStructureDefintion(String valueStructureId);
	
}
