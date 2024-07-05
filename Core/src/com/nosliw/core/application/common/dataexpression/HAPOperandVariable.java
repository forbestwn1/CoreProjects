package com.nosliw.core.application.common.dataexpression;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.core.application.common.valueport.HAPIdElement;

public interface HAPOperandVariable extends HAPOperand{
 
	@HAPAttribute
	public final static String VARIABLENAME = "variableName";
	
	@HAPAttribute
	public final static String VARIABLEID = "variableId";
	
	String getVariableName();
	
	HAPIdElement getVariableId();
	
}
