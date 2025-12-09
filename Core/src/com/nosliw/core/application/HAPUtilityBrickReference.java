package com.nosliw.core.application;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.path.HAPUtilityPath;

public class HAPUtilityBrickReference {

	public static void normalizeBrickReferenceInBundle(HAPIdBrickInBundle brickIdInBundle, String basePath, boolean processEnd, String brickRootNameIfNotProvided, HAPBundle currentBundle) {
		//add root name, task path
		HAPPath path = HAPUtilityBrickPath.normalizeBrickPath(new HAPPath(brickIdInBundle.getIdPath()), brickRootNameIfNotProvided, processEnd, currentBundle);
//		path = HAPUtilityTask.figureoutTaskPath(currentBundle, path, brickRootNameIfNotProvided);
		
		brickIdInBundle.setIdPath(path.getPath());
		calculateBrickIdInBundleRelativePath(brickIdInBundle, new HAPPath(basePath));
	}

	//calcluate relative path
	public static void calculateBrickIdInBundleRelativePath(HAPIdBrickInBundle brickRef, HAPPath blockPathFromRoot) {
		if(brickRef.getRelativePath()==null) {
			brickRef.setRelativePath(HAPUtilityPath.fromAbsoluteToRelativePath(brickRef.getIdPath(), blockPathFromRoot.toString()));
		}
	}
}
