package com.nosliw.core.application.common.scriptexpressio;

public class HAPUtilityScriptExpressionTraverse {

	public static void traverse(HAPExpressionScriptImp scriptExpression, HAPProcessorScriptExpressionSegment processor, Object value) {
		
		for(HAPSegmentScriptExpression segment : scriptExpression.getSegments()) {
			traverse(segment, processor, value);
		}
		
	}
	
	private static void traverse(HAPSegmentScriptExpression segment, HAPProcessorScriptExpressionSegment processor, Object value) {
		if(processor.process(segment, value)) {
			for(HAPSegmentScriptExpression child : segment.getChildren()) {
				traverse(child, processor, value);
			}
			
		}
		processor.postProcess(segment, value);
	}
	
}
