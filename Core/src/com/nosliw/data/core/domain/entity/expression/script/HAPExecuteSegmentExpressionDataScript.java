package com.nosliw.data.core.domain.entity.expression.script;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPExecuteSegmentExpressionDataScript implements HAPExecuteSegmentExpression{

	private List<HAPDefinitionSegmentExpression> m_segments;
	
	public HAPExecuteSegmentExpressionDataScript() {
		this.m_segments = new ArrayList<HAPDefinitionSegmentExpression>();
	}
	
	public void addSegmentScript(HAPDefinitionSegmentExpressionScript scriptSegment) {	this.m_segments.add(scriptSegment);	}
	
	public void addSegmentData(HAPDefinitionSegmentExpressionData dataSegment) {	this.m_segments.add(dataSegment);	}
	
	@Override
	public String getType() {  return HAPConstantShared.EXPRESSION_SEG_TYPE_DATASCRIPT;  }
	
}
