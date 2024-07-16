package com.nosliw.core.application.division.manual.common.scriptexpression;

public abstract class HAPManualProcessorScriptExpressionSegment {

	public abstract boolean process(HAPManualSegmentScriptExpression segment, Object value);
	
	void postProcess(HAPManualSegmentScriptExpression segment, Object value) {}
	
}
