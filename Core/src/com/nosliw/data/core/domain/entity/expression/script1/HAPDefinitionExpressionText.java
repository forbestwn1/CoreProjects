package com.nosliw.data.core.domain.entity.expression.script1;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPDefinitionExpressionText extends HAPDefinitionExpression{

	public HAPDefinitionExpressionText() {}
	
	public void addSegmentText(HAPDefinitionSegmentExpressionText textSegment) {	this.addSegment(textSegment);	}
	
	@Override
	public String getType() {  return HAPConstantShared.EXPRESSION_TYPE_TEXT;  }

}
