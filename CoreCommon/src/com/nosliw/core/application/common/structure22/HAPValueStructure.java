package com.nosliw.core.application.common.structure22;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPValueStructure extends HAPStructure{

	@HAPAttribute
	public static final String INITVALUE = "initValue";
	
	Object getInitValue(); 
	void setInitValue(Object initValue);
	
}
