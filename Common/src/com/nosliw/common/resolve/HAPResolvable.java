package com.nosliw.common.resolve;

import java.util.Map;

import com.nosliw.common.interpolate.HAPInterpolateProcessor;
import com.nosliw.common.interpolate.HAPInterpolateOutput;

public interface HAPResolvable {

	HAPInterpolateOutput resolveByPattern(Map<String, Object> patternDatas);
	HAPInterpolateOutput resolveByInterpolateProcessor(Map<HAPInterpolateProcessor, Object> patternDatas);
	
	boolean isResolved();
}
