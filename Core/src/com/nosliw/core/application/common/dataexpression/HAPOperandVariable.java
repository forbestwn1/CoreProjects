package com.nosliw.core.application.common.dataexpression;

import com.nosliw.common.constant.HAPAttribute;

public interface HAPOperandVariable extends HAPOperand{
 
	@HAPAttribute
	public final static String VARIABLENAME = "variableName";
	
	@HAPAttribute
	public final static String VARIABLEKEY = "variableKey";
	
	String getVariableName();
	
	String getVariableKey();
	
}
