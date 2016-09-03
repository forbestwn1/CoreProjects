package com.nosliw.common.interpolate;

/*
 * process interpolate through configure information
 */
public abstract class HAPPatternProcessorInterpolationByConfigure extends HAPPatternProcessorInterpolation{
	public HAPPatternProcessorInterpolationByConfigure(String startToken, String endToken){
		super(new HAPInterpolateProcessorByConfigure(startToken, endToken));
	}
}
