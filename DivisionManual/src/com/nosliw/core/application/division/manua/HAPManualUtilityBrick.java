package com.nosliw.core.application.division.manua;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPUtilityBundle;
import com.nosliw.core.application.division.manual.HAPInfoTreeNode;

public class HAPManualUtilityBrick {

	public static boolean isBrickComplex(HAPIdBrickType brickTypeId, HAPManualManagerBrick manualBrickMan) {
		return manualBrickMan.getBrickTypeInfo(brickTypeId).getIsComplex();
	}

	public static HAPComplexPath getBrickFullPathInfo(HAPInfoTreeNode treeNodeInfo) {
		HAPPath pathNorm = new HAPPath(treeNodeInfo.getPathFromRoot());
		Pair<String, HAPPath> pathPair = pathNorm.trimFirst();
		return new HAPComplexPath(HAPUtilityBundle.getBranchName(pathPair.getLeft()), pathPair.getRight());
	}
	
	
}
