package com.nosliw.core.application;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPUtilityResource;
import com.nosliw.core.runtime.HAPRuntimeInfo;
import com.nosliw.core.xxx.application.valueport.HAPInfoValuePortContainer;

public class HAPUtilityBrickValuePort {

	public static HAPInfoValuePortContainer getDescdentValuePortContainerInfo(HAPBundle bundle, String rootBrickNameIfNotProvide, HAPPath path, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPBrick brick = null;
		HAPResultBrickDescentValue brickDescentValueResult = HAPUtilityBrick.getDescdentBrickResult(bundle, path, rootBrickNameIfNotProvide);
		if(brickDescentValueResult.getBrick()!=null) {
			brick = brickDescentValueResult.getBrick();
			return new HAPInfoValuePortContainer(Pair.of(brick.getInternalValuePorts(), brick.getExternalValuePorts()), bundle.getValueStructureDomain(), true);
		}
		else if(brickDescentValueResult.getResourceId()!=null){
			HAPResourceDataBrick resourceData =(HAPResourceDataBrick)HAPUtilityResource.getResource(brickDescentValueResult.getResourceId(), resourceMan, runtimeInfo).getResourceData();
			brick = resourceData.getBrick();
			return new HAPInfoValuePortContainer(Pair.of(brick.getInternalValuePorts(), brick.getExternalValuePorts()), resourceData.getValueStructureDomain(), false);
		}
		else if(brickDescentValueResult.getDyanmicValue()!=null) {
			return new HAPInfoValuePortContainer(Pair.of(null, brickDescentValueResult.getDyanmicValue().getExternalValuePorts()), bundle.getValueStructureDomain(), true);
			
		}
		return null;
	}
	
}
