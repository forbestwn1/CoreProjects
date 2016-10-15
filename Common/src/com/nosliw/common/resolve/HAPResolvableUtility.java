package com.nosliw.common.resolve;

import java.util.Map;

import com.nosliw.common.interpolate.HAPInterpolateOutput;
import com.nosliw.common.interpolate.HAPInterpolateProcessor;
import com.nosliw.common.pattern.HAPPatternManager;

public class HAPResolvableUtility {
	public static HAPInterpolateOutput resolveByPatterns(String content, Map<String, Object> patternDatas){
		HAPInterpolateOutput out = new HAPInterpolateOutput();
		out.setOutput(content);
		if(content!=null){
			for(String patternName : patternDatas.keySet()){
				HAPInterpolateOutput result = resolveByPattern(out.getOutput(), patternName, patternDatas.get(patternName));
				out.setOutput(result.getOutput());
				out.addUnsolved(result.getUnsolved());
			}
		}
		return out;
	}
	
	public static HAPInterpolateOutput resolveByPattern(String content, String patternName, Object data){
		HAPInterpolateOutput out = new HAPInterpolateOutput();
		if(content!=null){
			out = (HAPInterpolateOutput)HAPPatternManager.getInstance().getPatternProcessor(patternName).parse(content, data);
		}
		return out;
	}
	
	
	public static HAPInterpolateOutput resolveByInterpolateProcessors(String content, Map<HAPInterpolateProcessor, Object> interpolateDatas){
		HAPInterpolateOutput out = new HAPInterpolateOutput();
		out.setOutput(content);
		if(content!=null){
			for(HAPInterpolateProcessor processor : interpolateDatas.keySet()){
				HAPInterpolateOutput result = resolveByInterpolateProcessor(out.getOutput(), processor, interpolateDatas.get(processor));
				out.setOutput(result.getOutput());
				out.addUnsolved(result.getUnsolved());
			}
		}
		return out;
	}

	public static HAPInterpolateOutput resolveByInterpolateProcessor(String content, HAPInterpolateProcessor processor, Object data){
		HAPInterpolateOutput out = new HAPInterpolateOutput();
		if(content!=null){
			out = processor.processExpression(content, data);
		}
		return out;
	}
}
