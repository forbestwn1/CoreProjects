package com.nosliw.common.strvalue.basic;

import java.util.Map;

import com.nosliw.common.interpolate.HAPInterpolateExpressionProcessor;
import com.nosliw.common.interpolate.HAPInterpolateOutput;

public interface HAPResolvable {

	public HAPInterpolateOutput resolveByPattern(Map<String, Object> patternDatas);
	public HAPInterpolateOutput resolveByInterpolateProcessor(Map<HAPInterpolateExpressionProcessor, Object> patternDatas);

	public boolean isStringResolved();

}
