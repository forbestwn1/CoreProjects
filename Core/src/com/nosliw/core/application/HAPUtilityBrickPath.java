package com.nosliw.core.application;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.common.task.HAPUtilityTask;

public class HAPUtilityBrickPath {

	public static HAPPath normalizeBrickPath(HAPPath path, String brickRootNameIfNotProvided, boolean processEnd, HAPBundle currentBundle) {
		//branch
		path = new HAPPath(HAPUtilityBundle.normalizePathWithBranch(path.getPath(), brickRootNameIfNotProvided));
		
		//task
		return HAPUtilityTask.normalizeBrickPathFroTask(path, processEnd, currentBundle);
	}
	
}
