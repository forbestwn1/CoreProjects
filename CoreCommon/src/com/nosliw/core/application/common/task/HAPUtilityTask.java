package com.nosliw.core.application.common.task;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.wrappertask.HAPBlockTaskWrapper;

public class HAPUtilityTask {

	public static HAPPath figureoutTaskPath(HAPBundle bundle, HAPPath idPath, String rootNameIfNotProvide) {
		HAPPath out = idPath;
		HAPBrick brick = HAPUtilityBrick.getDescdentBrickLocal(bundle, idPath, rootNameIfNotProvide);
		if(brick!=null&&brick.getBrickType().equals(HAPEnumBrickType.TASKWRAPPER_100)) {
			out = idPath.appendSegment(HAPBlockTaskWrapper.TASK);
		}
		return out;
	}
	
}
