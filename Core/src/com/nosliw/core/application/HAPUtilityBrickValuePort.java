package com.nosliw.core.application;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.common.valueport.HAPInfoValuePortContainer;
import com.nosliw.core.application.resource.HAPResourceDataBrick;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPUtilityBrickValuePort {

	public static HAPInfoValuePortContainer getDescdentValuePortContainerInfo(HAPBundle bundle, String rootBrickNameIfNotProvide, HAPPath path, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPBrick brick = null;
		HAPDomainValueStructure valueStructureDomain = null;
		HAPResultBrick brickResult = HAPUtilityBrick.getDescdentBrickResult(bundle, path, rootBrickNameIfNotProvide);
		if(brickResult.isInternalBrick()) {
			brick = brickResult.getBrick();
			valueStructureDomain = bundle.getValueStructureDomain();
		}
		else {
			HAPResourceDataBrick resourceData =(HAPResourceDataBrick)HAPUtilityResource.getResource(brickResult.getResourceId(), resourceMan, runtimeInfo).getResourceData();
			brick = resourceData.getBrick();
			valueStructureDomain = resourceData.getValueStructureDomain();
		}
		return new HAPInfoValuePortContainer(Pair.of(brick.getInternalValuePorts(), brick.getExternalValuePorts()), valueStructureDomain, brickResult.isInternalBrick());
	}
	
}
