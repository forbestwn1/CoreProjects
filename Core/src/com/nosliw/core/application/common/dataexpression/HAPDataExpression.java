package com.nosliw.core.application.common.dataexpression;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.core.application.common.withvariable.HAPWithVariable;

@HAPEntityWithAttribute
public interface HAPDataExpression extends HAPWithVariable, HAPSerializable{

	@HAPAttribute
	public static String OPERAND = "operand";
	
	HAPOperand getOperand();

}
