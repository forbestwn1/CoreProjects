package com.nosliw.data.core.entity.division.manual;

import com.nosliw.common.path.HAPPath;

public abstract class HAPManualProcessorEntityDownward {

	public abstract boolean processEntityNode(HAPManualInfoEntity rootEntityInfo, HAPPath path, Object data);

	public abstract void postProcessEntityNode(HAPManualInfoEntity rootEntityInfo, HAPPath path, Object data);

	protected boolean isRoot(HAPPath path) {
		return path==null||path.isEmpty();
	}
	
}
