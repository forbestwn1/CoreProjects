package com.nosliw.common.interpolate;

import com.nosliw.common.configure.HAPConfigureImp;

/*
 * process interpolate through configure information
 */
public abstract class HAPPatternProcessorInterpolationStatic extends HAPPatternProcessorInterpolation{
	public HAPPatternProcessorInterpolationStatic(String startToken, String endToken){
		super(new HAPInterpolateExpressionProcessor(startToken, endToken){
			@Override
			public String processIterpolate(String expression, Object object) {
				HAPConfigureImp varValues = (HAPConfigureImp)object;
				String varValue = varValues.getStringValue(expression);
				return varValue;
			}
		});
	}
	
}
