package com.nosliw.core.application.division.manual.common.scriptexpression;

public class HAPManualUtilityScriptExpression {

	public static void traverse(HAPManualExpressionScript scriptExpression, HAPManualProcessorScriptExpressionSegment processor, Object value) {
		
		for(HAPManualSegmentScriptExpression segment : scriptExpression.getSegments()) {
			traverse(segment, processor, value);
		}
		
	}
	
	private static void traverse(HAPManualSegmentScriptExpression segment, HAPManualProcessorScriptExpressionSegment processor, Object value) {
		if(processor.process(segment, value)) {
			for(HAPManualSegmentScriptExpression child : segment.getChildren()) {
				traverse(child, processor, value);
			}
			
		}
		processor.postProcess(segment, value);
	}
	
}
