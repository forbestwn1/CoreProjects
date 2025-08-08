package com.nosliw.core.xxx.application.valueport;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.valueport.HAPContainerValuePorts;
import com.nosliw.core.application.valueport.HAPIdElement;
import com.nosliw.core.application.valueport.HAPIdValuePortInBundle;
import com.nosliw.core.application.valueport.HAPInfoValuePortContainer;
import com.nosliw.core.application.valueport.HAPUtilityBrickValuePort;
import com.nosliw.core.application.valueport.HAPValuePort;
import com.nosliw.core.application.valueport.HAPWithInternalValuePort;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.runtime.HAPRuntimeInfo;
import com.nosliw.core.xxx.application.common.structure.HAPUtilityStructure;

public class HAPUtilityValuePort {

	
	public static HAPInfoValuePort getValuePortInBundle(HAPIdValuePortInBundle valuePortRef, HAPBundle bundle, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPInfoValuePortContainer valuePortContainerInfo = HAPUtilityBrickValuePort.getDescdentValuePortContainerInfo(bundle, null, new HAPPath(valuePortRef.getBrickId().getIdPath()), resourceMan, runtimeInfo);
		HAPContainerValuePorts valuePortContainer;
		if(HAPConstantShared.VALUEPORTGROUP_SIDE_INTERNAL.equals(valuePortRef.getValuePortSide())) {
			valuePortContainer = valuePortContainerInfo.getValuePortContainerPair().getLeft();
		}
		else {
			valuePortContainer = valuePortContainerInfo.getValuePortContainerPair().getRight();
		}
		return new HAPInfoValuePort(valuePortContainer.getValuePort(valuePortRef.getValuePortId()), valuePortContainerInfo.getValueStructureDomain());
	}
	

	public static HAPElementStructure getInternalElement(HAPIdElement varId, HAPWithInternalValuePort withInternalValuePort) {
		HAPValuePort valuePort = HAPUtilityValuePort.getInternalValuePort(varId, withInternalValuePort);
		HAPValueStructureInValuePort11111 valueStructureInValuePort = valuePort.getValueStructureDefintion(varId.getRootElementId().getValueStructureId());
		HAPElementStructure structureEle = HAPUtilityStructure.getDescendant(valueStructureInValuePort.getRoot(varId.getRootElementId().getRootName()).getDefinition(), varId.getElementPath().toString());
		return structureEle;
	}

	
	
	

	
	
}
