package com.nosliw.core.application.division.manual.core.b;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPUtilityBundle;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.a.HAPManualUtilityBrick;

public class HAPManualExeUtilityBrick {

	public static HAPManualBrick newBrickInstance(HAPIdBrickType brickTypeId, HAPBundle bundle, HAPManualManagerBrick manualBrickMan) {
		return manualBrickMan.newBrick(brickTypeId, bundle);
	}
	
	public static HAPManualBrick newRootBrickInstance(HAPIdBrickType brickTypeId, String rootName, HAPBundle bundle, HAPManualManagerBrick manualBrickMan) {
		HAPManualBrick brick = manualBrickMan.newBrick(brickTypeId, bundle);
		
		if(HAPUtilityBasic.isStringEmpty(rootName)) {
			brick.setTreeNodeInfo(new HAPInfoTreeNode());
		}
		else {
			brick.setTreeNodeInfo(new HAPInfoTreeNode(new HAPPath(HAPUtilityBundle.buildBranchPathSegment(rootName)), null));
		}
		return brick;
	}


	public static HAPBrick getDescdentBrickLocal(HAPBundle bundle, HAPInfoTreeNode treeNodeInfo) {
		HAPComplexPath pathInfo = HAPManualUtilityBrick.getBrickFullPathInfo(treeNodeInfo);
		return HAPUtilityBrick.getDescdentBrickLocal(bundle, pathInfo.getPath());
	}

}
