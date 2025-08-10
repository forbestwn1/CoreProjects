package com.nosliw.core.xxx.application.division.manual.core.b;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.division.manual.core.HAPInfoTreeNode;
import com.nosliw.core.application.division.manual.core.HAPManualUtilityBrick;

public class HAPManualExeUtilityBrick {

	


	public static HAPBrick getDescdentBrickLocal(HAPBundle bundle, HAPInfoTreeNode treeNodeInfo) {
		HAPComplexPath pathInfo = HAPManualUtilityBrick.getBrickFullPathInfo(treeNodeInfo);
		return HAPUtilityBrick.getDescdentBrickLocal(bundle, pathInfo.getPath());
	}

}
