package com.nosliw.data.core.domain.entity.expression.script1;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.info.HAPEntityInfoImp;

public abstract class HAPDefinitionExpression extends HAPEntityInfoImp{

	public final static String EXPRESSION = "expression";
	
	public final static String TYPE = "type";
	
	private List<HAPDefinitionSegmentExpression> m_segments;
	
	public HAPDefinitionExpression() {
		this.m_segments = new ArrayList<HAPDefinitionSegmentExpression>();
	}
	
	public abstract String getType();

	protected void addSegment(HAPDefinitionSegmentExpression segment) {	this.m_segments.add(segment);	}
	
	public List<HAPDefinitionSegmentExpression> getSegments(){    return this.m_segments;     }
	
}
