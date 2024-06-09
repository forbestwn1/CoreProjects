package com.nosliw.core.application.common.valueport;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.path.HAPUtilityPath;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPIdBrickInBundle;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.data.core.resource.HAPResourceManager;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPUtilityValuePort {

	public static HAPIdValuePortInBundle normalizeExternalValuePortId(HAPIdValuePortInBundle valuePortIdInBundle, String ioDirection, HAPPath blockPathFromRoot, HAPBundle currentBundle, HAPResourceManager resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPIdValuePortInBundle out = valuePortIdInBundle;
		if(out==null) {
			out = new HAPIdValuePortInBundle();
		}
		
		//normalize block reference
		HAPIdBrickInBundle brickId = out.getBrickId();
		if(brickId==null) {
			brickId = new HAPIdBrickInBundle(blockPathFromRoot.toString());
			out.setBlockId(brickId);
		}

		//normalize value port id
		HAPIdValuePortInBrick valuePortIdInBrick = out.getValuePortId();
		HAPBrick brick = HAPUtilityBrick.getDescdentBrick(currentBundle.getBrickWrapper(), new HAPPath(brickId.getIdPath()), resourceMan, runtimeInfo);
		valuePortIdInBrick = brick.getExternalValuePorts().normalizeValuePortId(valuePortIdInBrick, ioDirection);
		out.setValuePortId(valuePortIdInBrick);
		
		return out;
	}

	public static HAPIdValuePortInBundle normalizeInternalValuePortId(HAPIdValuePortInBundle valuePortIdInBundle, String ioDirection, HAPBrick brick) {
		HAPIdValuePortInBundle out = valuePortIdInBundle;
		if(out==null) {
			out = new HAPIdValuePortInBundle();
		}
		out.setValuePortId(brick.getInternalValuePorts().normalizeValuePortId(out.getValuePortId(), ioDirection));
		return out;
	}
	
	public static void normalizeValuePortRelativeBrickPath(HAPIdValuePortInBundle valuePortRef, HAPPath blockPathFromRoot) {
		HAPIdBrickInBundle brickRef = valuePortRef.getBrickId();
		if(brickRef.getRelativePath()==null) {
			brickRef.setRelativePath(HAPUtilityPath.fromAbsoluteToRelativePath(brickRef.getIdPath(), blockPathFromRoot.toString()));
		}
	}

	public static HAPValuePort getValuePortInBrick(HAPIdValuePortInBundle valuePortRef, HAPBrick brick) {
		return brick.getExternalValuePorts().getValuePort(valuePortRef.getValuePortId());
	}

	public static HAPValuePort getValuePortInBundle(HAPIdValuePortInBundle valuePortRef, HAPBundle bundle, HAPResourceManager resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPBrick brick = HAPUtilityBrick.getDescdentBrick(bundle.getBrickWrapper(), new HAPPath(valuePortRef.getBrickId().getIdPath()), resourceMan, runtimeInfo);
		return brick.getExternalValuePorts().getValuePort(valuePortRef.getValuePortId());
	}
	
}
