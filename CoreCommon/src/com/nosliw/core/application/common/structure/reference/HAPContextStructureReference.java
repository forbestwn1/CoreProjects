package com.nosliw.core.application.common.structure.reference;

import java.util.List;

import com.nosliw.core.application.valueport.HAPInfoValueStructureReference;
import com.nosliw.core.application.valueport.HAPReferenceValueStructure;
import com.nosliw.core.xxx.application1.division.manual.brick.valuestructure.HAPManualBrickValueStructure;

public interface HAPContextStructureReference {

	List<HAPInfoValueStructureReference> discoverCandidateValueStructure(HAPReferenceValueStructure valueStructureRef);
	
	HAPManualBrickValueStructure getValueStructureDefintion(String valueStructureId);
	
}
