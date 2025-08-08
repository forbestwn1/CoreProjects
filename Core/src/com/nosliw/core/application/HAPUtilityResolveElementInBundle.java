package com.nosliw.core.application;

import com.nosliw.core.application.common.structure.reference.HAPConfigureResolveElementReference;
import com.nosliw.core.application.valueport.HAPIdRootElement;
import com.nosliw.core.application.valueport.HAPInfoValuePort;
import com.nosliw.core.application.valueport.HAPReferenceElement;
import com.nosliw.core.application.valueport.HAPReferenceRootElement;
import com.nosliw.core.application.valueport.HAPResultReferenceResolve;
import com.nosliw.core.application.valueport.HAPUtilityBrickValuePort;
import com.nosliw.core.application.valueport.HAPUtilityResovleElement;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public class HAPUtilityResolveElementInBundle {

	public static HAPIdRootElement resolveRootReferenceInBundle(HAPReferenceRootElement rootEleCriteria, HAPConfigureResolveElementReference resolveConfigure, HAPBundle bundle, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo){
		HAPResultReferenceResolve resolve = analyzeElementReferenceInBundle(new HAPReferenceElement(rootEleCriteria), resolveConfigure, bundle, resourceMan, runtimeInfo);
		return new HAPIdRootElement(rootEleCriteria.getValuePortId(), resolve.structureId, rootEleCriteria.getRootName());
	}

	public static HAPResultReferenceResolve analyzeElementReferenceInBundle(HAPReferenceElement reference, HAPConfigureResolveElementReference resolveConfigure, HAPBundle bundle, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPInfoValuePort valuePortInfo = HAPUtilityBrickValuePort.getValuePortInBundle(reference.getValuePortId(), bundle, resourceMan, runtimeInfo);
		HAPResultReferenceResolve resolve = HAPUtilityResovleElement.analyzeElementReferenceValuePort(reference, valuePortInfo.getValuePort(), resolveConfigure, valuePortInfo.getValueStructureDomain());
		if(resolve!=null) {
			resolve.brickId = reference.getValuePortId().getBrickId();
		}
		return resolve;
	}

}
