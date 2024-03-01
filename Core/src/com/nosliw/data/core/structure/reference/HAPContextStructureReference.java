package com.nosliw.data.core.structure.reference;

import java.util.List;

import com.nosliw.data.core.domain.valueport.HAPReferenceValueStructure;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPDefinitionEntityValueStructure;

public interface HAPContextStructureReference {

	List<HAPInfoValueStructureReference> discoverCandidateValueStructure(HAPReferenceValueStructure valueStructureRef);
	
	HAPDefinitionEntityValueStructure getValueStructureDefintion(String valueStructureId);
	
}
