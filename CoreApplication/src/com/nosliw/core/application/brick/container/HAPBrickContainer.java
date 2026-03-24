package com.nosliw.core.application.brick.container;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPUtilityNosliw;
import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.HAPBrick;

//attribute: id or name of item for attribute name, otherwise, create attribute name
@HAPEntityWithAttribute
public interface HAPBrickContainer extends HAPBrick{

	@HAPAttribute
	static final public String ATTRINDEX = HAPUtilityNosliw.buildNosliwFullName("attrIndex");  

	List<HAPAttributeInBrick> getElements();
	
}
