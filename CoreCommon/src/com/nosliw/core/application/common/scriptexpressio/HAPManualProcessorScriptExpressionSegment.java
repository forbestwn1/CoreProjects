package com.nosliw.core.application.common.scriptexpressio;

public abstract class HAPManualProcessorScriptExpressionSegment {

	public abstract boolean process(HAPManualSegmentScriptExpression segment, Object value);
	
	void postProcess(HAPManualSegmentScriptExpression segment, Object value) {}
	
}
