package com.nosliw.core.xxx.application.valueport;

import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.valueport.HAPIdElement;
import com.nosliw.core.application.valueport.HAPValuePort;
import com.nosliw.core.application.valueport.HAPWithInternalValuePort;
import com.nosliw.core.xxx.application.common.structure.HAPUtilityStructure;

public class HAPUtilityValuePort {

	

	public static HAPElementStructure getInternalElement(HAPIdElement varId, HAPWithInternalValuePort withInternalValuePort) {
		HAPValuePort valuePort = HAPUtilityValuePort.getInternalValuePort(varId, withInternalValuePort);
		HAPValueStructureInValuePort11111 valueStructureInValuePort = valuePort.getValueStructureDefintion(varId.getRootElementId().getValueStructureId());
		HAPElementStructure structureEle = HAPUtilityStructure.getDescendant(valueStructureInValuePort.getRoot(varId.getRootElementId().getRootName()).getDefinition(), varId.getElementPath().toString());
		return structureEle;
	}

	
	
	

	
	
}
