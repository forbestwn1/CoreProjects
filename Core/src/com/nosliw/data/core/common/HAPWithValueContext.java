package com.nosliw.data.core.common;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public interface HAPWithValueContext {

	@HAPAttribute
	public static String VALUECONTEXT = "valueContext";

	HAPIdEntityInDomain getValueContextEntity();

	void setValueContextEntity(HAPIdEntityInDomain valueContextId);
	
}
