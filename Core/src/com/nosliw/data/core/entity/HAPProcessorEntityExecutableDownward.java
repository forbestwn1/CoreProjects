package com.nosliw.data.core.entity;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;

public abstract class HAPProcessorEntityExecutableDownward {

	public abstract boolean processEntityNode(HAPInfoEntity rootEntityInfo, HAPPath path, Object data);

	public abstract void postProcessEntityNode(HAPInfoEntity rootEntityInfo, HAPPath path, Object data);

	protected boolean isRoot(HAPPath path) {
		return path==null||path.isEmpty();
	}
	
	protected Pair<HAPEntityExecutable, String> getParentAttributeInfo(HAPEntityExecutable rootEntity, HAPPath path) {
		HAPEntityExecutable parentEntity = rootEntity;
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
			HAPEntityExecutable parentEntity = getEntityFromNode(rootNode);
			HAPPath childPath = new HAPPath();
			for(int i=0; i<pathSegs.length-1; i++) {
				childPath = childPath.appendSegment(pathSegs[i]);
			}
			return Pair.of(parentEntity.getDescendantAttribute(childPath), pathSegs[pathSegs.length-1]);
		}
	}
	
	protected HAPEntityExecutable getEntityFromNode(HAPTreeNode node) {
		HAPEntityExecutable out = null;
		Object value = node.getNodeValue();
		if(value instanceof HAPEntityExecutable) {
			out = (HAPEntityExecutable)value;
		}
		else if(value instanceof HAPWithEntity){
			out = ((HAPWithEntity)value).getEntity();
		}
		return out;
	}
}
