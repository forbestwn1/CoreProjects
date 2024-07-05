package com.nosliw.core.application.common.dataexpression;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;

@HAPEntityWithAttribute
public interface HAPOperand extends HAPSerializable{

	@HAPAttribute
	public static final String TYPE = "type"; 

	@HAPAttribute
	public static final String OUTPUTCRITERIA = "outputCriteria"; 

	//type of operand
	String getType();

	//operand output data type criteria
	HAPDataTypeCriteria getOutputCriteria();
	
}
