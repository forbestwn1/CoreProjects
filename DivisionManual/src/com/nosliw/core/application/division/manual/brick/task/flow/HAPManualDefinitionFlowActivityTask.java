package com.nosliw.core.application.division.manual.brick.task.flow;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.core.application.brick.task.flow.HAPTaskFlowNext;

public interface HAPManualDefinitionFlowActivityTask {

	@HAPAttribute
	public static final String NEXT = "next";

	HAPTaskFlowNext getNext();

}
