package com.nosliw.common.interpolate;

import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.pattern.HAPPatternManager;
import com.nosliw.common.strvalue.basic.HAPStringableValueUtility;

public class HAPInterpolateUtility {
	public static String interpolateByConfigure(String text, HAPConfigureImp configure){
		HAPInterpolateOutput out = new HAPInterpolateProcessorByConfigureForDoc().processExpression(text, configure);
		return out.getOutput();
	}
	
}
