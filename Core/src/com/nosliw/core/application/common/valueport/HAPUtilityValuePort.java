package com.nosliw.core.application.common.valueport;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.data.core.domain.HAPRefIdEntity;

public class HAPUtilityValuePort {

	public static HAPValuePort getValuePort(HAPIdValuePort valuePortId, HAPBundle bundle) {
		HAPBrick brick = bundle.getBrickByPath(new HAPPath(valuePortId.getEntityIdPath()));
		return brick.getValuePorts().getValuePort(valuePortId);
	}

	public static HAPValuePort getValuePort(HAPRefValuePort valuePortRef, HAPPath defaultBrickPathId, HAPBundle bundle) {
		HAPIdValuePort valuePortId = null;
		if(valuePortRef==null) {
			return getDefaultValuePortInEntity(bundle.getBrickByPath(defaultBrickPathId));
		}
		else {
			HAPRefIdEntity brickIdRef = valuePortRef.getEntityIdRef();
			if(brickIdRef==null) {
				brickIdRef = new HAPRefIdEntity(defaultBrickPathId.toString());
			}
			valuePortId = new HAPIdValuePort(valuePortRef);
		}
		return getValuePort(valuePortId, bundle);
	}

	public static HAPValuePort getDefaultValuePortInEntity(HAPBrick brick) {
		return brick.getValuePorts().getValuePort(null);
	}
	
}
