package com.nosliw.core.application.valueport;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPResourceDataBrick;
import com.nosliw.core.application.HAPResultBrickDescentValue;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPUtilityResource;
import com.nosliw.core.runtime.HAPRuntimeInfo;

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
	
}
