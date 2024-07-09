package com.nosliw.core.application.division.manual.executable;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualWrapperBrickRoot;

public class HAPManualExeUtilityBrick {

	public static HAPManualBrick newBrickInstance(HAPIdBrickType brickTypeId, HAPManualManagerBrick manualBrickMan) {
		return manualBrickMan.newBrick(brickTypeId);
	}
	
	public static HAPManualBrick newRootBrickInstance(HAPIdBrickType brickTypeId, HAPManualManagerBrick manualBrickMan) {
		HAPManualBrick brick = manualBrickMan.newBrick(brickTypeId);
		brick.setTreeNodeInfo(new HAPInfoTreeNode());
		return brick;
	}
	
	public static HAPTreeNodeBrick getDescdentTreeNode(HAPManualWrapperBrickRoot rootBrickWrapper, HAPPath path) {
		HAPTreeNodeBrick out = null;
		if(path==null || path.isEmpty()) {
			out = rootBrickWrapper;
		}
		else {
			out = (HAPManualAttributeInBrick)HAPUtilityBrick.getDescendantAttribute(rootBrickWrapper.getBrick(), path);
		}
		return out;
	}



}
