package com.nosliw.data.core.domain.entity.expression.script1;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPDefinitionSegmentExpressionDataScript implements HAPDefinitionSegmentExpression{

	private List<HAPDefinitionSegmentExpression> m_segments;
	
	public HAPDefinitionSegmentExpressionDataScript() {
		this.m_segments = new ArrayList<HAPDefinitionSegmentExpression>();
	}
	
	public void addSegmentScript(HAPDefinitionSegmentExpressionScript scriptSegment) {	this.m_segments.add(scriptSegment);	}
	
	public void addSegmentData(HAPDefinitionSegmentExpressionData dataSegment) {	this.m_segments.add(dataSegment);	}

	public List<HAPDefinitionSegmentExpression> getSegments(){    return this.m_segments;     }
	
	@Override
	public String getType() {  return HAPConstantShared.EXPRESSION_SEG_TYPE_DATASCRIPT;  }
	
}
