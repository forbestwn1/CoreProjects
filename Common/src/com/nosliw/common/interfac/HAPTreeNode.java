package com.nosliw.common.interfac;

import com.nosliw.common.path.HAPPath;

public interface HAPTreeNode {

	public static final String PATHFROMROOT = "pathFromRoot";

	public static final String NODEVALUE = "nodeValue";

	HAPPath getPathFromRoot();

	Object getNodeValue();
	
}
