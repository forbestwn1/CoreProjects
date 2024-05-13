package com.nosliw.core.application.common.valueport;

import org.apache.commons.lang3.tuple.Triple;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.path.HAPUtilityPath;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPReferenceBrickLocal;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.data.core.resource.HAPResourceManager;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPUtilityValuePort {

	public static Triple<HAPReferenceBrickLocal, HAPIdValuePort, HAPValuePort> getValuePort(HAPReferenceValuePort valuePortRef, HAPPath baseBrickPathId, HAPBundle bundle, HAPResourceManager resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPReferenceBrickLocal brickRef = null;
		HAPValuePort valuePort = null;
		HAPIdValuePort valuePortId = valuePortRef==null?null:valuePortRef.getValuePortId();
		
		//discover brick ref
		if(valuePortRef==null) {
			brickRef = new HAPReferenceBrickLocal(baseBrickPathId.toString());
		}
		else {
			brickRef = valuePortRef.getBrickReference();
			if(brickRef==null) {
				brickRef = new HAPReferenceBrickLocal(baseBrickPathId.toString());
			}
		}

		if(brickRef.getRelativePath()==null) {
			brickRef.setRelativePath(HAPUtilityPath.fromAbsoluteToRelativePath(brickRef.getIdPath(), baseBrickPathId.toString()));
		}

		if(brickRef.getIdPath()==null) {
			brickRef.setIdPath(HAPUtilityPath.fromRelativeToAbsolutePath(brickRef.getRelativePath(), baseBrickPathId.toString()));
		}

		HAPBrick brick = HAPUtilityBrick.getDescdentBrick(bundle.getBrickWrapper(), new HAPPath(brickRef.getIdPath()), resourceMan, runtimeInfo);
		
		//discover value port id
		if(valuePortId==null) {
			valuePortId = brick.getValuePorts().getDefaultValuePortId();
		}
		
		//value port
		valuePort = brick.getValuePorts().getValuePort(valuePortId);

		
		return Triple.of(brickRef, valuePortId, valuePort);
	}

	
	public static HAPValuePort getDefaultValuePortInEntity(HAPBrick brick) {
		return brick.getValuePorts().getValuePort(null);
	}
	
	public static HAPIdValuePort getValuePortId(HAPValuePort valuePort) {
		return new HAPIdValuePort(valuePort.getValuePortInfo().getType(), valuePort.getName());
	}
	
}
