package com.nosliw.core.application.brick.ui.uicontent;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.core.application.HAPBrick;

@HAPEntityWithAttribute
public interface HAPBlockComplexUICustomerTagDebugger extends HAPBrick, HAPEntityInfo{

	@HAPAttribute
	public static final String UITAGID = "uiTagId";
	@HAPAttribute
	public static final String ATTRIBUTE = "attribute";
	@HAPAttribute
	public static final String CHILD = "child";
	@HAPAttribute
	public static final String TAGDEFINITION = "tagDefinition";

}
