package com.nosliw.common.interpolate;

import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.pattern.HAPPatternManager;
import com.nosliw.common.strvalue.basic.HAPStringResolveUtility;

public class HAPInterpolateUtility {
	public static String interpolateByConfigure(String text, HAPConfigureImp configure){
		HAPInterpolateOutput out = HAPStringResolveUtility.resolveByPattern(text, HAPPatternProcessorDocVariable.class.getName(), configure);
		return out.getOutput();
	}
	
}
