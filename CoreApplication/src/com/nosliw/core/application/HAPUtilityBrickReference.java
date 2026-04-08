package com.nosliw.core.application;

import java.util.Map;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.path.HAPUtilityPath;

public class HAPUtilityBrickReference {

	public static void normalizeBrickReferenceInBundle(HAPIdBrickInBundle brickIdInBundle, String basePath, boolean processEnd, String brickRootNameIfNotProvided, Map<String, HAPPath> aliasMapping, HAPBundle currentBundle) {
		
		if(brickIdInBundle.getAlias()!=null && brickIdInBundle.getRelativePath()==null && brickIdInBundle.getIdPath()==null) {
			//only alias provided
			HAPPath path = aliasMapping.get(brickIdInBundle.getAlias());
			if(path==null) {
				throw new RuntimeException();
			}
			brickIdInBundle.setIdPath(path.getPath());
		}
		else {
			if(brickIdInBundle.getRelativePath()!=null&&brickIdInBundle.getIdPath()==null) {
		        //only relative path provided
				brickIdInBundle.setIdPath(HAPUtilityPath.fromRelativeToAbsolutePath(brickIdInBundle.getRelativePath(), basePath));
			}
			
			//normalize path: add root name, task path
			HAPPath path = HAPUtilityBrickPath.normalizeBrickPath(new HAPPath(brickIdInBundle.getIdPath()), brickRootNameIfNotProvided, processEnd, currentBundle);
			brickIdInBundle.setIdPath(path.getPath());
		}

		//recalculate relative path again even it may provided before
		brickIdInBundle.setRelativePath(HAPUtilityPath.fromAbsoluteToRelativePath(brickIdInBundle.getIdPath(), basePath));
	}

	//calcluate relative path
	public static void calculateBrickIdInBundleRelativePath(HAPIdBrickInBundle brickRef, HAPPath blockPathFromRoot) {
		if(brickRef.getRelativePath()==null) {
			brickRef.setRelativePath(HAPUtilityPath.fromAbsoluteToRelativePath(brickRef.getIdPath(), blockPathFromRoot.toString()));
		}
	}
}
