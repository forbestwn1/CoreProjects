package com.nosliw.core.application.division.manual.executable;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualUtilityBrick;

public class HAPManualExeUtilityBrick {

	public static HAPManualBrick newBrickInstance(HAPIdBrickType brickTypeId, HAPBundle bundle, HAPManualManagerBrick manualBrickMan) {
		return manualBrickMan.newBrick(brickTypeId, bundle);
	}
	
	public static HAPManualBrick newRootBrickInstance(HAPIdBrickType brickTypeId, HAPBundle bundle, HAPManualManagerBrick manualBrickMan) {
		HAPManualBrick brick = manualBrickMan.newBrick(brickTypeId, bundle);
		brick.setTreeNodeInfo(new HAPInfoTreeNode());
		return brick;
	}


	public static HAPBrick getDescdentBrickLocal(HAPBundle bundle, HAPInfoTreeNode treeNodeInfo) {
		HAPComplexPath pathInfo = HAPManualUtilityBrick.getBrickFullPathInfo(treeNodeInfo);
		return HAPUtilityBrick.getDescdentBrickLocal(bundle, pathInfo.getPath());
	}

}
