package com.nosliw.core.application.common.valueport;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.path.HAPUtilityPath;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPReferenceBrickLocal;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.data.core.resource.HAPResourceManager;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPUtilityValuePort {

	public static HAPReferenceValuePort normalizeValuePortReference(HAPReferenceValuePort valuePortRef, HAPPath blockPathFromRoot, HAPBundle currentBundle, HAPResourceManager resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPReferenceValuePort out = valuePortRef;
		if(out==null) {
			out = new HAPReferenceValuePort();
		}
		
		//normalize block reference
		HAPReferenceBrickLocal brickRef = out.getBrickReference();
		if(brickRef==null) {
			brickRef = new HAPReferenceBrickLocal(blockPathFromRoot.toString());
			out.setBlockReference(brickRef);
		}

		if(brickRef.getRelativePath()==null) {
			brickRef.setRelativePath(HAPUtilityPath.fromAbsoluteToRelativePath(brickRef.getIdPath(), blockPathFromRoot.toString()));
		}

		//normalize value port id
		HAPIdValuePort valuePortId = out.getValuePortId();
		if(valuePortId==null) {
			HAPBrick brick = HAPUtilityBrick.getDescdentBrick(currentBundle.getBrickWrapper(), new HAPPath(brickRef.getIdPath()), resourceMan, runtimeInfo);
			valuePortId = HAPUtilityValuePort.getValuePortId(HAPUtilityValuePort.getDefaultValuePortInEntity(brick));
			out.setValuePortId(valuePortId);
		}
		return out;
	}

	public static HAPValuePort getValuePort(HAPReferenceValuePort valuePortRef, HAPPath baseBrickPathId, HAPBundle bundle, HAPResourceManager resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPBrick brick = HAPUtilityBrick.getDescdentBrick(bundle.getBrickWrapper(), new HAPPath(valuePortRef.getBrickReference().getIdPath()), resourceMan, runtimeInfo);
		return brick.getValuePorts().getValuePort(valuePortRef.getValuePortId());
	}

	
	public static HAPValuePort getDefaultValuePortInEntity(HAPBrick brick) {
		return brick.getValuePorts().getValuePort(null);
	}
	
	public static HAPIdValuePort getValuePortId(HAPValuePort valuePort) {
		return new HAPIdValuePort(valuePort.getValuePortInfo().getType(), valuePort.getName());
	}
	
}
