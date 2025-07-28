package com.nosliw.core.application.brick.ui.uicontent;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPWithUIId {

	@HAPAttribute
	static final public String UIID = "UIId";  

	String getUIId();
	
}
