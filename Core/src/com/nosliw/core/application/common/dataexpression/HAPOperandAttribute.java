package com.nosliw.core.application.common.dataexpression;

public interface HAPOperandAttribute extends HAPOperand{

	public static final String ATTRIBUTE = "attribute";
	
	public static final String BASEDATA = "baseData";
	
	String getAttribute();
	
	HAPOperand getBase();

}
