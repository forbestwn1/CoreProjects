package com.nosliw.core.application.brick.wrapperbrick;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.HAPBrick;

@HAPEntityWithAttribute
public interface HAPBrickWrapperBrick extends HAPBrick, HAPEntityInfo{

	@HAPAttribute
	static final public String BRICK = "brick";  

	HAPEntityOrReference getBrick();

}
