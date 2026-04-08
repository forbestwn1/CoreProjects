package com.nosliw.core.application;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.path.HAPUtilityPath;

public class HAPUtilityBrickReference {

	public static void normalizeBrickReferenceInBundle(HAPIdBrickInBundle brickIdInBundle, String basePath, boolean processEnd, String brickRootNameIfNotProvided, HAPBundle currentBundle) {
        //path first
		if(brickIdInBundle.getRelativePath()!=null&&brickIdInBundle.getIdPath()==null) {
			brickIdInBundle.setIdPath(HAPUtilityPath.fromRelativeToAbsolutePath(brickIdInBundle.getRelativePath(), basePath));
        }
		
		//add root name, task path
		HAPPath path = HAPUtilityBrickPath.normalizeBrickPath(new HAPPath(brickIdInBundle.getIdPath()), brickRootNameIfNotProvided, processEnd, currentBundle);
		brickIdInBundle.setIdPath(path.getPath());

		//recalcuate relative path again even it may provided before
		brickIdInBundle.setRelativePath(HAPUtilityPath.fromAbsoluteToRelativePath(brickIdInBundle.getIdPath(), basePath));
	}

	//calcluate relative path
	public static void calculateBrickIdInBundleRelativePath(HAPIdBrickInBundle brickRef, HAPPath blockPathFromRoot) {
		if(brickRef.getRelativePath()==null) {
			brickRef.setRelativePath(HAPUtilityPath.fromAbsoluteToRelativePath(brickRef.getIdPath(), blockPathFromRoot.toString()));
		}
	}
}
