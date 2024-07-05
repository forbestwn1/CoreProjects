package com.nosliw.core.application.common.dataexpression;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.data.HAPData;

public interface HAPOperandConstant extends HAPOperand{

	@HAPAttribute
	public final static String DATA = "data"; 
	
	HAPData getData();
	
}
