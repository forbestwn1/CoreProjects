package com.nosliw.core.application.division.manual;

import com.nosliw.common.path.HAPPath;

public abstract class HAPManualProcessorEntityDownward {

	public abstract boolean processEntityNode(HAPManualWrapperBrick rootEntityInfo, HAPPath path, Object data);

	public void postProcessEntityNode(HAPManualWrapperBrick rootEntityInfo, HAPPath path, Object data) {}

	protected boolean isRoot(HAPPath path) {
		return path==null||path.isEmpty();
	}
	
}
