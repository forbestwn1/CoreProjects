package com.nosliw.data.core.domain.entity.expression.script;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPExecutableExpressionText extends HAPExecutableExpression{

	public HAPExecutableExpressionText() {}
	
	public void addSegmentText(HAPExecutableSegmentExpressionText textSegment) {	this.addSegment(textSegment);	}
	
	@Override
	public String getType() {  return HAPConstantShared.EXPRESSION_TYPE_TEXT;  }

}
