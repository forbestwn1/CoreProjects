package com.nosliw.core.application.division.manual;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualAttributeInBrick;
import com.nosliw.core.application.division.manual.executable.HAPTreeNodeBrick;

public class HAPManualUtilityBrick {

	public static boolean isBrickComplex(HAPIdBrickType brickTypeId, HAPManualManagerBrick manualBrickMan) {
		return manualBrickMan.getBrickTypeInfo(brickTypeId).getIsComplex();
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
