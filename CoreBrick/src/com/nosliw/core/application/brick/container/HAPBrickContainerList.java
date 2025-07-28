package com.nosliw.core.application.brick.container;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPUtilityNosliw;

@HAPEntityWithAttribute
public interface HAPBrickContainerList extends HAPBrickContainer{

	@HAPAttribute
	static final public String ATTRSORT = HAPUtilityNosliw.buildNosliwFullName("attrSort");  
	
}
