package com.nosliw.common.interpolate;

import com.nosliw.common.configure.HAPConfigureImp;

public class HAPInterpolateProcessorByConfigure extends HAPInterpolateProcessor{

	public HAPInterpolateProcessorByConfigure(String startToken, String endToken) {
		super(startToken, endToken);
	}

	@Override
	public String processIterpolate(String expression, Object object) {
		HAPConfigureImp varValues = (HAPConfigureImp)object;
		String varValue = varValues.getStringValue(expression);
		return varValue;
	}
}
