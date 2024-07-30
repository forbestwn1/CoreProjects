package com.nosliw.core.application;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;

public abstract class HAPHandlerDownward {

	public abstract boolean processBrickNode(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path, Object data);

	public abstract void postProcessBrickNode(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path, Object data);

	protected boolean isRoot(HAPPath path) {
		return path==null||path.isEmpty();
	}
	
	protected Pair<HAPBrick, String> getParentAttributeInfo(HAPWrapperBrickRoot rootEntityWrapper, HAPPath path) {
		HAPPath childPath = new HAPPath();
		String[] pathSegs = path.getPathSegments();
		for(int i=0; i<pathSegs.length-1; i++) {
			childPath = childPath.appendSegment(pathSegs[i]);
		}
		
		HAPBrick parentBrick = HAPUtilityBrick.getDescdentBrickLocal(rootEntityWrapper, childPath);
		return Pair.of(parentBrick, pathSegs[pathSegs.length-1]);
	}
}
