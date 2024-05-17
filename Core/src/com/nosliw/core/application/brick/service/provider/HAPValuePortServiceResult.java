package com.nosliw.core.application.brick.service.provider;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.interactive.HAPResultInInteractiveInterface;
import com.nosliw.core.application.common.interactive1.HAPValuePortInteractiveResult;
import com.nosliw.core.application.common.valueport.HAPIdValuePort;
import com.nosliw.core.application.common.valueport.HAPInfoValuePort;

public class HAPValuePortServiceResult extends HAPValuePortInteractiveResult{

	public HAPValuePortServiceResult(String entityId, HAPResultInInteractiveInterface result) {
		super(new HAPIdValuePort(entityId, HAPConstantShared.VALUEPORT_TYPE_SERVICE_RESULT, result.getName()), new HAPInfoValuePort(), result);
	}

}
