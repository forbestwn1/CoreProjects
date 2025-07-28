package com.nosliw.data.core.domain.entity.expression.script;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPDefinitionExpressionScript extends HAPDefinitionExpression{

	@Override
	public String getType() {   return HAPConstantShared.EXPRESSION_TYPE_SCRIPT;  }

	public void addSegmentDataScript(HAPDefinitionSegmentExpressionDataScript dataScriptSegment) {	this.addSegment(dataScriptSegment);	}
	
}
