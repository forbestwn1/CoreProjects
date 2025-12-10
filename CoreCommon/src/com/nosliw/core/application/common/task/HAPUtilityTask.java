package com.nosliw.core.application.common.task;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.wrappertask.HAPBlockTaskWrapper;

public class HAPUtilityTask {

	//get descdent task brick (if path refer to task wrapper, then expend path to task in wrapper)
	public static HAPBrick getDescdentBrickLocalTask(HAPBundle bundle, HAPPath idPath, String rootNameIfNotProvide) {
		return HAPUtilityBrick.getDescdentBrickLocal(bundle, HAPUtilityTask.figureoutTaskPath(bundle, idPath, rootNameIfNotProvide), rootNameIfNotProvide);		
	}
	
	//get task path (if path reference to task wrapper, then expend path to task in wrapper)
	private static HAPPath figureoutTaskPath(HAPBundle bundle, HAPPath idPath, String rootNameIfNotProvide) {
		HAPPath out = idPath;
		HAPBrick brick = HAPUtilityBrick.getDescdentBrickLocal(bundle, idPath, rootNameIfNotProvide);
		if(brick!=null&&brick.getBrickType().equals(HAPEnumBrickType.TASKWRAPPER_100)) {
			out = idPath.appendSegment(HAPBlockTaskWrapper.TASK);
		}
		return out;
	}
	
}
