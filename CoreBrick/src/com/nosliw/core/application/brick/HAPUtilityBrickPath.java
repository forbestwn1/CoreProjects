package com.nosliw.core.application.brick;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPUtilityBundle;
import com.nosliw.core.application.brick.wrappertask.HAPBlockTaskWrapper;
import com.nosliw.core.application.common.brick.HAPBrickImp;

public class HAPUtilityBrickPath {

	public static HAPPath normalizeBrickPath(HAPPath path, String brickRootNameIfNotProvided, boolean processEnd, HAPBundle currentBundle) {
		HAPPath out = new HAPPath();
		
		HAPComplexPath pathInfo = HAPUtilityBundle.getBrickFullPathInfo(path.toString(), brickRootNameIfNotProvided);
		out = out.appendSegment(HAPUtilityBundle.buildBranchPathSegment(pathInfo.getRoot()));
		HAPBrickImp brick = (HAPBrickImp)currentBundle.getRootBrickWrapper(pathInfo.getRoot()).getBrick();
		
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
		
		return new HAPPath(HAPUtilityBundle.normalizePathWithBranch(out.toString(), brickRootNameIfNotProvided));
	}
	
}
