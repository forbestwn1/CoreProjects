package com.nosliw.data.core.domain.entity.expression.script;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPDefinitionSegmentExpressionScript implements HAPDefinitionSegmentExpression{

	private String m_script;

	//store segment elements
	//   string
	//   script constant
	//   script variable
	private List<Object> m_segments;

	public HAPDefinitionSegmentExpressionScript(String script) {
		this.m_script = script;
		this.m_segments = new ArrayList<Object>();
	}
	
	@Override
	public String getType() {  return HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPT;  }

	public String getScript() {    return this.m_script;     }

	public List<Object> getSegments(){   return this.m_segments;     }
	
	public void addSegment(Object segment) {    this.m_segments.add(segment);    }
	
}
