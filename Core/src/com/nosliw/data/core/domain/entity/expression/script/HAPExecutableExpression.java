package com.nosliw.data.core.domain.entity.expression.script;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;

public abstract class HAPExecutableExpression extends HAPExecutableImpEntityInfo{

	public final static String EXPRESSION = "expression";
	
	public final static String TYPE = "type";
	
	private List<HAPExecutableSegmentExpression> m_segments;
	
	public HAPExecutableExpression() {
		this.m_segments = new ArrayList<HAPExecutableSegmentExpression>();
	}
	
	public abstract String getType();

	protected void addSegment(HAPExecutableSegmentExpression segment) {	this.m_segments.add(segment);	}
	
	public List<HAPExecutableSegmentExpression> getSegments(){    return this.m_segments;     }
	
}
