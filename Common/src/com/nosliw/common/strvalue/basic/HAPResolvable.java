package com.nosliw.common.strvalue.basic;

import java.util.Map;

import com.nosliw.common.interpolate.HAPInterpolateProcessor;
import com.nosliw.common.interpolate.HAPInterpolateOutput;

public interface HAPResolvable {

	public abstract HAPInterpolateOutput resolveByPattern(Map<String, Object> patternDatas);
	public abstract HAPInterpolateOutput resolveByInterpolateProcessor(Map<HAPInterpolateProcessor, Object> patternDatas);
	
	public abstract boolean isResolved();
}
