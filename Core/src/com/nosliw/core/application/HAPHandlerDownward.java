package com.nosliw.core.application;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;

public abstract class HAPHandlerDownward {

	public abstract boolean processBrickNode(HAPWrapperBrick brickWrapper, HAPPath path, Object data);

	public abstract void postProcessBrickNode(HAPWrapperBrick brickWrapper, HAPPath path, Object data);

	protected boolean isRoot(HAPPath path) {
		return path==null||path.isEmpty();
	}
	
	protected Pair<HAPBrick, String> getParentAttributeInfo(HAPBrick rootEntity, HAPPath path) {
		HAPBrick parentEntity = rootEntity;
		HAPPath childPath = new HAPPath();
		String[] pathSegs = path.getPathSegments();
		for(int i=0; i<pathSegs.length-1; i++) {
			childPath = childPath.appendSegment(pathSegs[i]);
		}
		return Pair.of(parentEntity.getDescendantEntity(childPath), pathSegs[pathSegs.length-1]);
	}
	
	protected Pair<HAPTreeNode, String> getNodePathInfo(HAPTreeNode rootNode, HAPPath path) {
		String[] pathSegs = path.getPathSegments();
		if(path.isEmpty()) {
			return Pair.of(rootNode, null);
		}
		else if(pathSegs.length<=1) {
			return Pair.of(rootNode, path.getPath());
		}
		else {
			HAPBrick parentEntity = getBrickFromNode(rootNode);
			HAPPath childPath = new HAPPath();
			for(int i=0; i<pathSegs.length-1; i++) {
				childPath = childPath.appendSegment(pathSegs[i]);
			}
			return Pair.of(parentEntity.getDescendantAttribute(childPath), pathSegs[pathSegs.length-1]);
		}
	}
	
	protected HAPBrick getBrickFromNode(HAPTreeNode node) {
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
