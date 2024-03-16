package com.nosliw.core.application.division.manual;

import com.nosliw.common.path.HAPPath;

public abstract class HAPManualProcessorEntityDownward {

	public abstract boolean processEntityNode(HAPManualInfoEntity rootEntityInfo, HAPPath path, Object data);

	public void postProcessEntityNode(HAPManualInfoEntity rootEntityInfo, HAPPath path, Object data) {}

	protected boolean isRoot(HAPPath path) {
		return path==null||path.isEmpty();
	}
	
}
