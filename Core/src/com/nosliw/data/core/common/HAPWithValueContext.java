package com.nosliw.data.core.common;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public interface HAPWithValueContext {

	@HAPAttribute
	public static String VALUECONTEXT = "valueContext";

	@HAPAttribute
	public static String ISBORDER = "isBorder";

	boolean getIsBorder();
	
	void setIsBorder(boolean isBorder);
	
	HAPIdEntityInDomain getValueContextEntity();

	void setValueContextEntity(HAPIdEntityInDomain valueContextId);
	
}
