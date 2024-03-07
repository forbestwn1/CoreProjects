package com.nosliw.data.core.entity;

import com.nosliw.common.path.HAPPath;

public interface HAPTreeNode {

	HAPPath getPathFromRoot();

	HAPEntityExecutable getEntity();
	
}
