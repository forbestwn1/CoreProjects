package com.nosliw.data.core.domain.entity.expression.script;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPExecuteExpressionText extends HAPExecuteExpression{

	public HAPExecuteExpressionText() {}
	
	public void addSegmentText(HAPDefinitionSegmentExpressionText textSegment) {	this.addSegment(textSegment);	}
	
	@Override
	public String getType() {  return HAPConstantShared.EXPRESSION_TYPE_TEXT;  }

}
