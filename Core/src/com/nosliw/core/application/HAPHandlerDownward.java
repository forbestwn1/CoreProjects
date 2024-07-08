package com.nosliw.core.application;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.division.manual.executable.HAPBrick;
import com.nosliw.core.application.division.manual.executable.HAPTreeNodeBrick;

public abstract class HAPHandlerDownward {

	public abstract boolean processBrickNode(HAPWrapperBrickRoot brickWrapper, HAPPath path, Object data);

	public abstract void postProcessBrickNode(HAPWrapperBrickRoot brickWrapper, HAPPath path, Object data);

	protected boolean isRoot(HAPPath path) {
		return path==null||path.isEmpty();
	}
	
	protected HAPBrick getBrickFromNode(HAPTreeNodeBrick node) {
		HAPBrick out = null;
		Object value = node.getNodeValue();
		if(value instanceof HAPBrick) {
			out = (HAPBrick)value;
		}
		else if(value instanceof HAPWithBrick){
			out = ((HAPWithBrick)value).getBrick();
		}
		return out;
	}
}
