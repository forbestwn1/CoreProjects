package com.nosliw.data.core.domain.entity.expression.script;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPExecuteExpressionLiterate extends HAPExecuteExpression{

	@Override
	public String getType() {	return HAPConstantShared.EXPRESSION_TYPE_LITERATE;	}

	public void addSegmentText(HAPDefinitionSegmentExpressionText textSegment) {	this.addSegment(textSegment);	}

	public void addSegmentDataScript(HAPDefinitionSegmentExpressionDataScript dataScriptSegment) {	this.addSegment(dataScriptSegment);	}

}
