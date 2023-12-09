package com.nosliw.data.core.domain.entity;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.data.core.component.HAPContextProcessor;

public abstract class HAPProcessorEntityExecutableDownward {

	public abstract boolean processEntityNode(HAPExecutableEntity rootEntity, HAPPath path, HAPContextProcessor processContext);

	public abstract void postProcessEntityNode(HAPExecutableEntity rootEntity, HAPPath path, HAPContextProcessor processContext);

	protected boolean isRoot(HAPPath path) {
		return path==null||path.isEmpty();
	}
	
	protected Pair<HAPExecutableEntity, String> getParentAttributeInfo(HAPExecutableEntity rootEntity, HAPPath path) {
		HAPExecutableEntity parentEntity = rootEntity;
		HAPPath childPath = new HAPPath();
		String[] pathSegs = path.getPathSegments();
		for(int i=0; i<pathSegs.length-1; i++) {
			childPath = childPath.appendSegment(pathSegs[i]);
		}
		return Pair.of(parentEntity.getDescendantEntity(childPath), pathSegs[pathSegs.length-1]);
	}
	
}
