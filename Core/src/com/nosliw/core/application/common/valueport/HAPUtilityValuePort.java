package com.nosliw.core.application.common.valueport;

import org.apache.commons.lang3.tuple.Triple;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.path.HAPUtilityPath;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPReferenceBrickLocal;
import com.nosliw.core.application.HAPUtilityBrick;

public class HAPUtilityValuePort {

	public static Triple<HAPReferenceBrickLocal, HAPIdValuePort, HAPValuePort> getValuePort(HAPReferenceValuePort valuePortRef, HAPPath baseBrickPathId, HAPBundle bundle) {
		HAPReferenceBrickLocal brickRef = null;
		HAPValuePort valuePort = null;
		HAPIdValuePort valuePortId = valuePortRef==null?null:valuePortRef.getValuePortId();
		

		if(valuePortRef==null) {
			brickRef = new HAPReferenceBrickLocal(baseBrickPathId.toString());
		}
		else {
			brickRef = valuePortRef.getBrickReference();
			if(brickRef==null) {
				brickRef = new HAPReferenceBrickLocal(baseBrickPathId.toString());
			}
		}

		HAPBrick brick = HAPUtilityBrick.getBrick(brickRef, baseBrickPathId.toString(), bundle);
		
		if(valuePortId==null) {
			valuePortId = brick.getValuePorts().getDefaultValuePortId();
		}
		
		valuePort = brick.getValuePorts().getValuePort(valuePortId);

		if(brickRef.getRelativePath()==null) {
			brickRef.setRelativePath(HAPUtilityPath.fromAbsoluteToRelativePath(baseBrickPathId.toString(), brickRef.getIdPath()));
		}

		if(brickRef.getIdPath()==null) {
			brickRef.setIdPath(HAPUtilityPath.fromRelativeToAbsolutePath(baseBrickPathId.toString(), brickRef.getRelativePath()));
		}
		
		return Triple.of(brickRef, valuePortId, valuePort);
	}

	
	public static HAPValuePort getDefaultValuePortInEntity(HAPBrick brick) {
		return brick.getValuePorts().getValuePort(null);
	}
	
	public static HAPIdValuePort getValuePortId(HAPValuePort valuePort) {
		return new HAPIdValuePort(valuePort.getValuePortInfo().getType(), valuePort.getName());
	}
	
}
