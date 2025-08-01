package com.nosliw.core.application.brick.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.data.HAPData;

//contains all information related with service definition
@HAPEntityWithAttribute
public interface HAPBlockData extends HAPBrick{

	@HAPAttribute
	public static final String DATA = "data";
	
	HAPData getData();

}
