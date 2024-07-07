package com.nosliw.core.application.common.dataexpression;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.core.application.common.valueport.HAPIdElement;

@HAPEntityWithAttribute
public interface HAPDataExpression extends HAPSerializable{

	@HAPAttribute
	public static String OPERAND = "operand";
	
	@HAPAttribute
	public static String VARIABLEIDS = "variableIds";
	
	public HAPOperand getOperand();

	List<HAPIdElement> getVariablesInfo();

}
