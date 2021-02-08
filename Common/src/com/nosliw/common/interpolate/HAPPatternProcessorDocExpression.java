package com.nosliw.common.interpolate;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPPatternProcessorDocExpression extends HAPPatternProcessorInterpolationDynamic{

	public HAPPatternProcessorDocExpression(){
		super(HAPConstantShared.SEPERATOR_EXPRESSIONSTART, HAPConstantShared.SEPERATOR_EXPRESSIONEND);
	}

}
