package com.nosliw.core.application.common.task;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPUtilityBundle;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.wrappertask.HAPBlockTaskWrapper;
import com.nosliw.core.application.common.brick.HAPBrickImp;

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
	
	public static HAPPath normalizeBrickPathFroTask(HAPPath path, boolean processEnd, HAPBundle currentBundle) {
		HAPPath out = new HAPPath();
		
		HAPComplexPath pathInfo = new HAPComplexPath(path.getPath());
		out = out.appendSegment(pathInfo.getRoot());
		HAPBrickImp brick = (HAPBrickImp)currentBundle.getRootBrickWrapper(HAPUtilityBundle.getBranchName(pathInfo.getRoot())).getBrick();
		
		String[] segs = pathInfo.getPathSegs();
		int i = -1;
		do {
			if(brick!=null) {
				if(brick.getBrickType().equals(HAPEnumBrickType.TASKWRAPPER_100)) {
					//task wrapper
					
					if(!(i==segs.length-1&&!processEnd)) {
						if(i==segs.length-1||!segs[i+1].equals(HAPBlockTaskWrapper.TASK)) {
							out = out.appendSegment(HAPBlockTaskWrapper.TASK);
						}
					}
				}
			}
			
			i++;
			
			if(i<segs.length) {
				if(brick!=null) {
					brick = (HAPBrickImp)brick.getAttributeValueOfBrickLocal(segs[i]);
				}
				out = out.appendSegment(segs[i]);
			}
		}while(i<segs.length);
		return out;
	}
	
}
