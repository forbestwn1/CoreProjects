package com.nosliw.data.core.domain.entity.expression.script;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPExecutableExpressionScript extends HAPExecutableExpression{

	@Override
	public String getType() {   return HAPConstantShared.EXPRESSION_TYPE_SCRIPT;  }

	public void addSegmentDataScript(HAPDefinitionSegmentExpressionDataScript dataScriptSegment) {	this.addSegment(dataScriptSegment);	}
	
}
