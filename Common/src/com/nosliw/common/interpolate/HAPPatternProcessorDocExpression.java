package com.nosliw.common.interpolate;

import com.nosliw.common.utils.HAPConstant;

public class HAPPatternProcessorDocExpression extends HAPPatternProcessorInterpolationDynamic{

	public HAPPatternProcessorDocExpression(){
		super(HAPConstant.SEPERATOR_EXPRESSIONSTART, HAPConstant.SEPERATOR_EXPRESSIONEND);
	}

}
