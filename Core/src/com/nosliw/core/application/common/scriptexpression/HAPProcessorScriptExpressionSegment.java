package com.nosliw.core.application.common.scriptexpression;

public abstract class HAPProcessorScriptExpressionSegment {

	public abstract boolean process(HAPSegmentScriptExpression segment, Object value);
	
	void postProcess(HAPSegmentScriptExpression segment, Object value) {}
	
}