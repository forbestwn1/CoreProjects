package com.nosliw.core.application;

import com.nosliw.common.path.HAPPath;

public abstract class HAPHandlerDownward {

	public abstract boolean processBrickNode(HAPWrapperBrick brickWrapper, HAPPath path, Object data);

	public abstract void postProcessBrickNode(HAPWrapperBrick brickWrapper, HAPPath path, Object data);

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
