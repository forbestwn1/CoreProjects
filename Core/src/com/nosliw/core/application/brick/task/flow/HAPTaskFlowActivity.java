package com.nosliw.core.application.brick.task.flow;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfo;

public interface HAPTaskFlowActivity extends HAPEntityInfo{

	@HAPAttribute
	public static final String NEXT = "next";
	
	HAPTaskFlowNext getNext();
	
}
