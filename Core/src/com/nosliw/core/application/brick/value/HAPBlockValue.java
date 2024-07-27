package com.nosliw.core.application.brick.value;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrick;

//contains all information related with service definition
@HAPEntityWithAttribute
public interface HAPBlockValue extends HAPBrick{

	@HAPAttribute
	public static final String VALUE = "value";
	
	Object getValue();

}
